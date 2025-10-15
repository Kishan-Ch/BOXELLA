async function signUp() {

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const user = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        confirmPassword: confirmPassword
    };

    const userJSON = JSON.stringify(user);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",
                body: userJSON,
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.staus) {
            window.location = "signUp.html";
        } else {
            document.getElementById("message").innerHTML = json.message;
        }
    } else {
        document.getElementById("message").innerHTML = "Registration failed. Please try again";
    }


}