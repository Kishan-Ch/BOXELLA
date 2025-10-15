async function sendEmail() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const email = document.getElementById("email").value;

    const response = await fetch("SendEmail?email=" + email);

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
//            console.log(json);
            notyf.success(json.message);

        } else {
            notyf.error(json.message);
            
        }
    } else {
        notyf.error(json.message);
    }
}

async function verifyAccount(){
    
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const code = document.getElementById("code").value;
//    console.log(code);

    const response = await fetch("VerifyAccount?vcode=" + code);
    
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
             notyf.success(json.message);
             window.location = "resetPassword.html";
        } else {
             notyf.error(json.message);
        }
    } else {
         notyf.error(json.message);
    }
}