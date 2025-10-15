// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    notyf.success("Payment completed. OrderID:" + orderId);

};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    notyf.error("Error:" + error);

};



async function loadCartItems() {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");

    const response = await fetch("LoadCartItems?boxId=" + boxId);
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json.cartItems);
            const cart_item_container = document.getElementById("cart-item-container");
            cart_item_container.innerHTML = "";
            json.cartItems.forEach(cart => {
                let full_price = cart.item.price * cart.qty;

                let tableData = `<tr class="table-cart-row" id="cart-item-row">
                                            <td class="product-col">
                                                <div class="product">
                                                    <figure class="product-media">
                                                        <a href="#">
                                                            <img src="assets/product-images/${cart.item.id}/image1.jpeg" alt="Product image">
                                                        </a>
                                                    </figure>

                                                    <h3 class="product-title">
                                                        <a href="#">${cart.item.name}</a>
                                                    </h3><!-- End .product-title -->
                                                </div><!-- End .product -->
                                            </td>
                                            <td class="price-col">Rs. ${new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigitis: 2}).format(cart.item.price)}</td>
                                            <td class="quantity-col" style="font-size:16px;">
                                                    ${cart.qty}
                                            </td>
                                            <td class="total-col" style="font-size:14px;">Rs. ${full_price}.00</td>
                                            <td class="remove-col text-center"><button class="btn-remove text-danger" onclick="deleteItem(${cart.item.id});"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                                                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>
                                                    <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>
                                                    </svg></button></td>
                                        </tr>`;
                cart_item_container.innerHTML += tableData;

            });


        } else {
//            console.log(json);

            notyf.error(json.message);
        }
    } else {
        notyf.error("Cart item loading failed");
    }



}


async function deleteItem(itemId) {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");
//    console.log(id+" "+boxId);

    const response = await fetch("DeleteCartItems?id=" + itemId + "&boxId=" + boxId);

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            notyf.success(json.message);
            window.location.reload();
        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error("Cart item loading failed");
    }


}

async function loadCheckoutData() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });
    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");

    const response = await fetch("LoadCheckoutData?boxId=" + boxId);

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            const userAddress = json.userAddress;
            const cityList = json.cityList;
            const cartItems = json.cartItems;
            const giftBox = json.giftBox;

            // load cities
            let city_select = document.getElementById("city-select");
            cityList.forEach(city => {
                let option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                city_select.appendChild(option);

            });


            const current_adddress_checkbox = document.getElementById("checkbox1");
            current_adddress_checkbox.addEventListener("change", function () {
                let first_name = document.getElementById("first-name");
                let last_name = document.getElementById("last-name");
                let line_one = document.getElementById("line-one");
                let line_two = document.getElementById("line-two");
                let mobile = document.getElementById("mobile");
                let postal_code = document.getElementById("postal-code");

                if (current_adddress_checkbox.checked) {
                    first_name.value = userAddress.user.firstName;
                    last_name.value = userAddress.user.lastName;
                    city_select.value = userAddress.city.id;
                    city_select.dispatchEvent(new Event("change"));
                    line_one.value = userAddress.line1;
                    line_two.value = userAddress.line2;
                    if (userAddress.mobile !== undefined) {
                        mobile.value = userAddress.mobile;
                    } else {
                        mobile.value = "";
                    }
                    postal_code.value = userAddress.postalCode;
                } else {
                    first_name.value = "";
                    last_name.value = "";
                    city_select.value = "";
                    city_select.dispatchEvent(new Event("change"));
                    line_one.value = "";
                    line_two.value = "";
                    mobile.value = "";
                    postal_code.value = "";
                }
            });

            //cart prices
            let item_body = document.getElementById("item-body");
            let sub_item = document.getElementById("sub-item");
            let sub_total = document.getElementById("sub-total");
            let full_amount = document.getElementById("full-amount");
            let box_price = document.getElementById("box-price");
            let box_name = document.getElementById("box-name");

            box_name.innerHTML = `+(Box)  ${giftBox.name} <span class="pull-right" style="float: right;" id="box-price">Rs. ${new Intl.NumberFormat(
                    "en-US",
                    {minimumFractionDigitis: 2}).format(giftBox.price)}</span>`;

            item_body.innerHTML = "";
            let total = 0;
            let shipAmount = 500;
            cartItems.forEach(cart => {
                let sub_item_clone = sub_item.cloneNode(true);
                sub_item_clone.style.display = 'flex';
                sub_item_clone.querySelector("#sub-item-name").innerHTML = cart.item.name;
                sub_item_clone.querySelector("#sub-item-qty").innerHTML = cart.qty;

                let item_subTot = Number(cart.qty) * Number(cart.item.price);

                sub_item_clone.querySelector("#sub-item-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigitis: 2}).format(item_subTot);

                item_body.appendChild(sub_item_clone);
                total += item_subTot;

            });
            console.log(total + giftBox.price);
            sub_total.innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {minimumFractionDigitis: 2}).format(total + giftBox.price);

            let subTot = total + giftBox.price;
            full_amount.innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {minimumFractionDigitis: 2}).format(subTot + shipAmount);

        } else {
            if (json.message === "empty-cart") {
                notyf.error("Please add Items First");
                window.location = "index.html";
            } else {
                notyf.error(json.message);
            }
        }

    } else {
        if (response.status === 401) {
            window.location = "signIn.html";
        }
    }
}



async function checkout() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");


    let amountText = document.getElementById("full-amount").textContent;
    const amount = amountText.replace("Rs.", "").trim();
    let checkBox1 = document.getElementById("checkbox1").checked;
    let first_name = document.getElementById("first-name");
    let last_name = document.getElementById("last-name");
    let line_one = document.getElementById("line-one");
    let line_two = document.getElementById("line-two");
    let mobile = document.getElementById("mobile");
    let postal_code = document.getElementById("postal-code");
    let city_select = document.getElementById("city-select");

    let data = {
        isCurrentAddress: checkBox1,
        firstName: first_name.value,
        lastName: last_name.value,
        citySelect: city_select.value,
        lineOne: line_one.value,
        lineTwo: line_two.value,
        mobile: mobile.value,
        postalCode: postal_code.value,
        amount: amount,
        boxId: boxId
    };

    let dataJSON = JSON.stringify(data);

    const response = await fetch("Checkout", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: dataJSON
    });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json.message);
//            notyf.success(json.message);
            //payhere process
             payhere.startPayment(json.payhereJson);
        } else {
            
                notyf.error(json.message);
            
        }
    } else {
        notyf.error("Somrthing went wrong. Please try again");
    }


}