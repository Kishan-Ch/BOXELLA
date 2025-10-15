
async function loadData() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);

    const boxId = searchParam.get("boxId");
    const boxName = searchParam.get("boxName");

    document.getElementById("boxImg").src = `assets/product-images/giftBox-images/${boxId}/image1.jpeg`;
    document.getElementById("box-title").innerHTML = boxName;

    const response = await fetch("LoadItemData");
    if (response.ok) {
        const json = await response.json();
        loadOptions(json.categoryList);
        updateProductView(json, boxId);

    } else {
        notyf.error(json.message);
    }
}

//
//const prod_card = document.getElementById("prod_card");
//let pegination_buttn = document.getElementById("pegination-buttn");
//let currentPage = 0;
//
//async function loadData() {
//    const notyf = new Notyf({
//        duration: 4000,
//        position: {x: "right", y: "top"},
//        ripple: true,
//    });
//
//    const searchParam = new URLSearchParams(window.location.search);
//
//    const boxId = searchParam.get("boxId");
//    const boxName = searchParam.get("boxName");
//
//    document.getElementById("boxImg").src = `assets/product-images/giftBox-images/${boxId}/image1.jpeg`;
//    document.getElementById("box-title").innerHTML = boxName;
//
//    const response = await fetch("LoadItemData");
//    if (response.ok) {
//        const json = await response.json();
//
//        loadOptions(json.categoryList);
//
//        if (json.hasOwnProperty("itemList") && json.itemList !== undefined) {
//            document.getElementById("boxImg").src = `assets/product-images/giftBox-images/${boxId}/image1.jpeg`;
//            document.getElementById("box-title").innerHTML = boxName;
//            document.getElementById("productCount").innerHTML = json.productCount;
//
//            const itemCard = document.getElementById("itemCard");
//            const pagination_container = document.getElementById("pagination-container");
//
//            const item_per_page = 4;
//            const allProductCount = json.productCount;
//            const pages = Math.ceil(allProductCount / item_per_page);
//
//            function renderPage(page) {
//                currentPage = page;
//                itemCard.innerHTML = "";
//
//                let start = currentPage * item_per_page;
//                let end = start + item_per_page;
//                let paginatedItems = json.itemList.slice(start, end);
//
//                paginatedItems.forEach(items => {
//                    let itemCardDesign = `<div class="product product-list" id="itemCardbox">
//                        <div class="row">
//                            <div class="col-6 col-lg-3">
//                                <figure class="product-media">
//                                    <a href="singleProduct.html?id=${items.id}">
//                                        <img src="assets/product-images/${items.id}/image1.jpeg" alt="Product image" class="product-image">
//                                    </a>
//                                </figure>
//                            </div>
//                            <div class="col-6 col-lg-3 order-lg-last mt-5">
//                            
//                                <div class="product-action mt-5 mx-4">
//                                    <label for="qty-${items.id}">Qty:</label>
//                                    <div class="product-details-quantity">
//                                        <input type="number" style="font-size: 14px; margin-left: 14px;" class="form-control qty-input" id="qty-${items.id}" value="1" min="1" max="10" step="1" data-decimals="0" required>
//                                    </div>
//                                </div>
//                                <div class="product-list-action">
//                                
//                                    <a href="#" class="btn-product btn-cart" style="text-decoration: none;"><span>add to Box</span></a>
//                                </div>
//                            </div>
//                            <div class="col-lg-6">
//                                <div class="product-body product-action-inner">
//                                    <div class="product-cat">
//                                        <a href="#" id="product-cat" style="font-weight: 450;">${items.category.name}</a>
//                                    </div>
//                                    <h3 class="product-title">
//                                        <a href="singleProduct.html?id=${items.id}" id="product-a2">${items.name}</a>
//                                    </h3>
//                                    <div class="product-content">
//                                        <p id="product-des">${items.description}</p>
//                                    </div>
//                                    <div class="product-nav product-nav-thumbs gap-3">
//                                        <a href="#" class="active">
//                                            <img src="assets/product-images/${items.id}/image2.jpeg" alt="product desc" id="product-image2">
//                                        </a>
//                                        <a href="#">
//                                            <img src="assets/product-images/${items.id}/image3.jpeg" alt="product desc" id="product-image3">
//                                        </a>
//                                        <a href="#">
//                                            <img src="assets/product-images/${items.id}/image4.jpeg" alt="product desc" id="product-image4">
//                                        </a>
//                                    </div>
//                                </div>
//                                <div class="product-price mt-5" style="font-weight: 580; font-size: 20px;">
//                                    Rs. ${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(items.price)}
//                                </div>
//                            </div>
//                        </div>
//                    </div>`;
//
//                    itemCard.innerHTML += itemCardDesign;
//                });
//
//                // Render pagination buttons
//                pagination_container.innerHTML = "";
//
//                // Prev button
//                if (currentPage !== 0) {
//                    let prev_button_clone = pegination_buttn.cloneNode(true);
//                    prev_button_clone.innerHTML = "Prev";
//                    prev_button_clone.addEventListener("click", e => {
//                        e.preventDefault();
//                        renderPage(currentPage - 1);
//                    });
//                    pagination_container.appendChild(prev_button_clone);
//                }
//
//                // Numbered pagination buttons
//                for (let i = 0; i < pages; i++) {
//                    let pagination_buttn_clone = pegination_buttn.cloneNode(true);
//                    pagination_buttn_clone.innerHTML = i + 1;
//                    pagination_buttn_clone.addEventListener("click", e => {
//                        e.preventDefault();
//                        renderPage(i);
//                    });
//
//                    if (i === currentPage) {
//                        pagination_buttn_clone.className = "page-link page-link-next";
//                        pagination_buttn_clone.style.cssText = `
//                            display: flex;
//                            align-items: center;
//                            gap: 5px;
//                            padding: 8px 14px;
//                            background: rgb(182, 139, 98);
//                            color: #fff;
//                            border-radius: 5px;
//                            text-decoration: none;
//                            font-size: 14px;
//                        `;
//                    } else {
//                        pagination_buttn_clone.className = "page-link page-link-next";
//                        pagination_buttn_clone.style.cssText = `
//                            display: flex;
//                            align-items: center;
//                            gap: 5px;
//                            padding: 8px 14px;
//                            background: #f1f1f1;
//                            border-radius: 5px;
//                            text-decoration: none;
//                            color: #555;
//                            font-size: 14px;
//                        `;
//                    }
//                    pagination_container.appendChild(pagination_buttn_clone);
//                }
//
//                // Next button
//                if (currentPage !== (pages - 1)) {
//                    let pagination_next_btn = pegination_buttn.cloneNode(true);
//                    pagination_next_btn.innerHTML = "Next";
//                    pagination_next_btn.addEventListener("click", e => {
//                        e.preventDefault();
//                        renderPage(currentPage + 1);
//                    });
//                    pagination_container.appendChild(pagination_next_btn);
//                }
//            }
//
//            // Initial render
//            renderPage(currentPage);
//
//        } else {
//            notyf.error(json.message);
//        }
//    } else {
//        notyf.error("Failed to load data");
//    }
//}




function loadOptions(dataList) {
    let options = document.getElementById("cat-options");
    let itemlabel = document.getElementById("cat-item");

    options.innerHTML = "";

    dataList.forEach((item, index) => {
        let itemClone = itemlabel.cloneNode(true);
        itemClone.style.display = "";

        let input = itemClone.querySelector("input");
        let label = itemClone.querySelector("label");

        let uniqueId = "cat-radio-" + index;

        input.id = uniqueId;
        input.name = "cat-radio";
        label.htmlFor = uniqueId;

        label.textContent = item.name;

        options.appendChild(itemClone);
    });


    const all_items = document.querySelectorAll("#cat-options .cat-item");

    all_items.forEach(item => {
        item.addEventListener("click", function () {
            all_items.forEach(i => {
                i.classList.remove("checked");
            });
            this.classList.add("checked");
        });
    });
}

//
//function updateProductView(json) {
//    const product_container = document.getElementById("itemCard");
//    product_container.innerHTML = "";
//
//    let item_per_page = 4;
//    let start = currentPage * item_per_page;
//    let end = start + item_per_page;
//
//    json.itemList.slice(start, end).forEach(item => {
//        let item_clone = prod_card.cloneNode(true);
//        item_clone.querySelector("#product-a").href = "singleProduct.html?id=" + item.id;
//        item_clone.querySelector("#product-image").src = "assets/product-images/" + item.id + "/image1.jpeg";
//        item_clone.querySelector("#product-image2").src = "assets/product-images/" + item.id + "/image2.jpeg";
//        item_clone.querySelector("#product-image3").src = "assets/product-images/" + item.id + "/image3.jpeg";
//        item_clone.querySelector("#product-image4").src = "assets/product-images/" + item.id + "/image4.jpeg";
//        item_clone.querySelector("#product-cart").addEventListener(
//                "click", (e) => {
//            addToBox(item.id);
//            e.preventDefault();
//        });
//        item_clone.querySelector("#product-a2").href = "singleProduct.html?id=" + item.id;
//        item_clone.querySelector("#product-a2").innerHTML = item.name;
//        item_clone.querySelector("#product-des").innerHTML = item.description;
//        item_clone.querySelector("#product-cat").innerHTML = item.category.name;
//        item_clone.querySelector("#product-price").innerHTML = "Rs. " + new Intl.NumberFormat(
//                "en-US",
//                {minimumFractionDigits: 2}).format(item.price);
//        item_clone.querySelector("#product-cart").onclick = function (e) {
//            addToItemBox(item.id, 1);
//            e.preventDefault();
//        };
//
//        product_container.appendChild(item_clone);
//    });
//
//    let pagination_container = document.getElementById("pagination-container");
//    pagination_container.innerHTML = "";
//
//    let allProductCount = json.productCount;
//    document.getElementById("productCount").innerHTML = allProductCount;
//
//    let pages = Math.ceil(allProductCount / item_per_page);
//
//    // prev button
//    if (currentPage !== 0) {
//        let prev_button_clone = pegination_buttn.cloneNode(true);
//        prev_button_clone.innerHTML = "Prev";
//        prev_button_clone.addEventListener(
//                "click", (e) => {
//            currentPage--;
//            searchProduct(currentPage * item_per_page);
//            e.preventDefault();
//        });
//        pagination_container.appendChild(prev_button_clone);
//    }
//
//    // pagination button
//    for (let i = 0; i < pages; i++) {
//        let pagination_buttn_clone = pegination_buttn.cloneNode(true);
//        pagination_buttn_clone.innerHTML = i + 1;
//        pagination_buttn_clone.addEventListener(
//                "click", (e) => {
//            searchProduct(i * item_per_page);
//            e.preventDefault();
//        });
//
//        if (i === Number(currentPage)) {
//            pagination_buttn_clone.className = "page-link page-link-next";
//            pagination_buttn_clone.style.cssText = `
//                display: flex;
//                align-items: center;
//                gap: 5px;
//                padding: 8px 14px;
//                background: rgb(182, 139, 98);
//                color: #fff;
//                border-radius: 5px;
//                text-decoration: none;
//                font-size: 14px;`;
//        } else {
//            pagination_buttn_clone.className = "page-link page-link-next";
//            pagination_buttn_clone.style.cssText = `
//                display: flex;
//                align-items: center;
//                gap: 5px;
//                padding: 8px 14px;
//                background: #f1f1f1;
//                border-radius: 5px;
//                text-decoration: none;
//                color: #555;
//                font-size: 14px;`;
//        }
//
//        pagination_container.appendChild(pagination_buttn_clone);
//    }
//
//    // next button
//    if (currentPage !== (pages - 1)) {
//        let pagination_next_btn = pegination_buttn.cloneNode(true);
//        pagination_next_btn.innerHTML = "Next";
//        pagination_next_btn.addEventListener(
//                "click", (e) => {
//            currentPage++;
//            searchProduct(currentPage * item_per_page);
//            e.preventDefault();
//        });
//
//        pagination_container.appendChild(pagination_next_btn);
//    }
//}

async function searchProduct(firstResult) {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);

    const boxId = searchParam.get("boxId");

    const category_name = document.getElementById("cat-options").querySelector("input:checked")?.nextElementSibling.innerHTML;

    const price_range_start = $("#slider-range").slider("values", 0); //left
    const price_range_end = $("#slider-range").slider("values", 1); // right

    const sort_value = document.getElementById("sortby").value;

    const data = {
        firstResult: firstResult,
        categoryName: category_name,
        priceStart: price_range_start,
        priceEnd: price_range_end,
        sortValue: sort_value
    };

    const dataJSON = JSON.stringify(data);

    const response = await fetch("SearchProduct",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
//            console.log(json);
            updateProductView(json, boxId);
        } else {
            notyf.error("error");
        }
    } else {
        notyf.error(json.message);
    }
}




const prod_card = document.getElementById("prod_card");
let pegination_buttn = document.getElementById("pegination-buttn");
let currentPage = 0;

function updateProductView(json, boxId) {
    const product_container = document.getElementById("itemCard");
    product_container.innerHTML = "";

    let item_per_page = 4;
    let allProductCount = json.productCount;
    let pages = Math.ceil(allProductCount / item_per_page);


    if (currentPage >= pages)
        currentPage = 0;

    let pageItems = json.itemList;

    if (pageItems.length > item_per_page) {
        let start = currentPage * item_per_page;
        let end = start + item_per_page;
        pageItems = pageItems.slice(start, end);
    }


    pageItems.forEach(item => {
        let item_clone = prod_card.cloneNode(true);

        item_clone.querySelector("#product-a").href = `singleProduct.html?id=${item.id}`;
        item_clone.querySelector("#product-image").src = `assets/product-images/${item.id}/image1.jpeg`;
        item_clone.querySelector("#product-image2").src = `assets/product-images/${item.id}/image2.jpeg`;
        item_clone.querySelector("#product-image3").src = `assets/product-images/${item.id}/image3.jpeg`;
        item_clone.querySelector("#product-image4").src = `assets/product-images/${item.id}/image4.jpeg`;

        item_clone.querySelector("#product-a2").href = `singleProduct.html?id=${item.id}`;
        item_clone.querySelector("#product-a2").textContent = item.name;

        item_clone.querySelector("#product-des").textContent = item.description;
        item_clone.querySelector("#product-cat").textContent = item.category.name;
        item_clone.querySelector("#product-price").textContent = "Rs. " + new Intl.NumberFormat("en-US", {
            minimumFractionDigits: 2
        }).format(item.price);

        item_clone.querySelector("#product-cart").onclick = function (e) {
            addToItemBox(item.id, 1, boxId, item.price, item.size);
            e.preventDefault();
        };

        product_container.appendChild(item_clone);
    });

    document.getElementById("productCount").textContent = allProductCount;

    // Pagination container setup
    let pagination_container = document.getElementById("pagination-container");
    pagination_container.innerHTML = "";

    // Prev button
    if (currentPage > 0) {
        let prev_button_clone = pegination_buttn.cloneNode(true);
        prev_button_clone.textContent = "Prev";
        prev_button_clone.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage--;
            searchProduct(currentPage * item_per_page);
        });
        pagination_container.appendChild(prev_button_clone);
    }

    // Page buttons
    for (let i = 0; i < pages; i++) {
        let pageBtn = pegination_buttn.cloneNode(true);
        pageBtn.textContent = (i + 1);
        pageBtn.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage = i;
            searchProduct(currentPage * item_per_page);
        });

        if (i === currentPage) {
            pageBtn.className = "page-link page-link-next";
            pageBtn.style.cssText = `
                display: flex;
                align-items: center;
                gap: 5px;
                padding: 8px 14px;
                background: rgb(182, 139, 98);
                color: #fff;
                border-radius: 5px;
                text-decoration: none;
                font-size: 14px;`;
        } else {
            pageBtn.className = "page-link page-link-next";
            pageBtn.style.cssText = `
                display: flex;
                align-items: center;
                gap: 5px;
                padding: 8px 14px;
                background: #f1f1f1;
                border-radius: 5px;
                text-decoration: none;
                color: #555;
                font-size: 14px;`;
        }

        pagination_container.appendChild(pageBtn);
    }

    // Next button
    if (currentPage < pages - 1) {
        let next_button_clone = pegination_buttn.cloneNode(true);
        next_button_clone.textContent = "Next";
        next_button_clone.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage++;
            searchProduct(currentPage * item_per_page);
        });
        pagination_container.appendChild(next_button_clone);
    }
}



let cartTotals = {};

//product-cart
async function addToItemBox(productId, qty, boxId, itemPrice, size) {

    const progressBar = document.getElementById('progressBar');
    const progressText = document.getElementById('progressText');

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const response = await fetch("AddtoBox?id=" + productId + "&qty=" + qty + "&boxId=" + boxId + "&itemPrice=" + itemPrice + "&size=" + size);
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            notyf.success(json.message);

            if (json.totalAmount !== undefined) {
                // Save or update this product's total amount
                cartTotals[productId] = json.totalAmount;

                // Calculate grand total
                let grandTotal = Object.values(cartTotals).reduce((sum, val) => sum + val, 0);

                console.log("Grand Total:", grandTotal.toFixed(2));

                // Update UI if element exists
                const priceElem = document.getElementById("cart-add-price");
                if (priceElem) {
                    priceElem.innerText = `Rs. ${grandTotal.toFixed(2)}`;
                    priceElem.style.cssText = "font-size: 15px; font-weight: bold;";
                }
            }

//            progressBar.style.width = value + '%';
//            progressText.textContent = value + '%';



        } else {
            notyf.error(json.message);
        }
    } else {
        notyf.error("Something went wrone. Try again!");
    }


}



function handleClick() {
    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");

    window.location = "cart.html?boxId=" + boxId;


}

async function seachTextData() {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");

    const search = document.getElementById("search").value;

    const response = await fetch("SearchItems?search=" + search);

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            updateProductView(json, boxId);
        } else {
            if (json.message === "Search text is empty") {
                window.location.reload();
            } else {
                notyf.error(json.message);
            }
        }
    } else {
        notyf.error("Something went wrone. Try again!");
    }
}