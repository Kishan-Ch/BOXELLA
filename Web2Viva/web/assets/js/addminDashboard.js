async function loadData() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });
    const response = await fetch("LoadAdminData");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            const categorySelect = document.getElementById("category");
            const name = document.getElementById("name").innerHTML = json.userData.firstName + " " + json.userData.lastName;

            json.categoryList.forEach(item => {
                const categoryOption = document.createElement("option");
                categoryOption.value = item.id;
                categoryOption.innerHTML = item.name;
                categorySelect.appendChild(categoryOption);
            });



        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error("Unable to loading");
    }

}

async function addProduct() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });
    const prName = document.getElementById("prName").value;
    const prDesc = document.getElementById("prDesc").value;
    const category = document.getElementById("category").value;
    const price = document.getElementById("price").value;
    const qty = document.getElementById("qty").value;

    const img1 = document.getElementById("img1").files[0];
    const img2 = document.getElementById("img2").files[0];
    const img3 = document.getElementById("img3").files[0];
    const img4 = document.getElementById("img4").files[0];

    const form = new FormData();
    form.append("prName", prName);
    form.append("prDesc", prDesc);
    form.append("category", category);
    form.append("price", price);
    form.append("qty", qty);
    form.append("img1", img1);
    form.append("img2", img2);
    form.append("img3", img3);
    form.append("img4", img4);

    const response = await fetch("SaveProduct",
            {
                method: "POST",
                body: form
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            notyf.success(json.message);
            window.location.reload();
        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error("Product Adding failed!");
    }

}


async function addGiftBox() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const boxName = document.getElementById("boxName").value;
    const boxDesc = document.getElementById("boxDesc").value;
    const boxprice = document.getElementById("boxprice").value;
    const boxQty = document.getElementById("boxQty").value;
    
    const Boximg1 = document.getElementById("Boximg1").files[0];
    const Boximg2 = document.getElementById("Boximg2").files[0];
    const Boximg3 = document.getElementById("Boximg3").files[0];
    const Boximg4 = document.getElementById("Boximg4").files[0];

    const form = new FormData();
    form.append("boxName", boxName);
    form.append("boxDesc", boxDesc);
    form.append("boxprice", boxprice);
    form.append("boxQty", boxQty);
    form.append("Boximg1", Boximg1);
    form.append("Boximg2", Boximg2);
    form.append("Boximg3", Boximg3);
    form.append("Boximg4", Boximg4);

    const response = await fetch("AddGiftBox",
            {
                method: "POST",
                body: form
                
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            notyf.success(json.message);     
            window.location.reload();

        } else {
            notyf.error(json.message);
        }
    } else {
//        console.log(json.message);
        notyf.error("Gift box Adding failed!");
    }

}