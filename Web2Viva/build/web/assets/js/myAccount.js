function loadData() {
    getUserData();
    getCityData();

}

async function getUserData() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const response = await fetch("MyAccount");
    if (response.ok) {
        const json = await response.json();

        document.getElementById("username").innerHTML = `${json.firstName} ${json.lastName}`;
        document.getElementById("dashEmail").innerHTML = `Member since ${json.since}`;
        document.getElementById("firstName").value = json.firstName;
        document.getElementById("lastName").value = json.lastName;
        document.getElementById("email").value = json.email;

        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {
//            console.log("ok");
            let line1;
            let line2;
            let postalCode;
            let cityId;

            json.addressList.forEach(address => {
                line1 = address.line1;
                line2 = address.line2;
                postalCode = address.postalCode;
                cityId = address.city.id;
            });
            
            console.log(line1);
            console.log(line2);
            console.log(postalCode);
            console.log(cityId);
            
            document.getElementById("line1").value = line1;
            document.getElementById("line2").value = line2;
            document.getElementById("line2").value = line2;
            document.getElementById("citySelect").value = parseInt(cityId);
            document.getElementById("postalCode").value = postalCode;
            
            
        }

    } else {
         if (response.status === 401) {
            window.location = "signIn.html";
        }
    }
}


async function getCityData() {
    const response = await fetch("CityData");
    if (response.ok) {
        const json = await response.json();
        const citySelect = document.getElementById("citySelect");
        json.forEach(city => {
            let option = document.createElement("option");
            option.innerHTML = city.name;
            option.value = city.id;
            citySelect.appendChild(option);
        });
    }
}

async function saveChanges() {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const line1 = document.getElementById("line1").value;
    const line2 = document.getElementById("line2").value;
    const cityId = document.getElementById("citySelect").value;
    const postalCode = document.getElementById("postalCode").value;

    const userDataObject = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        line1: line1,
        line2: line2,
        cityId: cityId,
        postalCode: postalCode
    };

    const userDataJSON = JSON.stringify(userDataObject);

    const response = await fetch("MyAccount", {
        method: "PUT",
        headers: {
            "Content-Type": "applicaton/json"
        },
        body: userDataJSON
    });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            getUserData();
            notyf.success("User Profile details Updated");
        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error("Something went wrong!");
    }


}