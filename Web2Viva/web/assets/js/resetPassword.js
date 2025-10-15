async function confirmPassord() {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const passwords = {
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    const passwordJson = JSON.stringify(passwords);

    const response = await fetch(
            "ResetPassword", {
                method: "POST",
                body: passwordJson,
                header: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            notyf.success(json.message);
            window.location = "signIn.html";
        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error(json.message);
    }
}
