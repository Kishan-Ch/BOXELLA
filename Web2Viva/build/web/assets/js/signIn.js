async function signIn() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const signIn = {
        email: email,
        password: password
    };

    const signInJson = JSON.stringify(signIn);

    const response = await fetch(
            "SignIn",
            {
                method: "POST",
                body: signInJson,
                header: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {

        const json = await response.json();

        if (json.status) {

            if (json.message === "Admin") {
                window.location = "adminDashboard.html";

            } else {
                window.location = "index.html";
            }

        } else {
            document.getElementById("message").innerHTML = json.message;
        }

    } else {
        document.getElementById("message").innerHTML = "Sign In failed. Please try again";
    }
}