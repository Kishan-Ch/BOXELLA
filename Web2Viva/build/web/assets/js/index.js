let currentBoxPage = 0;
const boxItemPerPage = 8;
const pegination_buttn = document.getElementById("pegination-buttn");

async function loadBoxData(offset = 0) {
    currentBoxPage = Math.floor(offset / boxItemPerPage);

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const response = await fetch("LoadBoxData");
    if (response.ok) {
        const json = await response.json();

        if (json.hasOwnProperty("boxList") && json.boxList !== undefined) {
            const boxCard = document.getElementById("boxCard");
            boxCard.innerHTML = "";

            let totalBoxes = json.productCount || json.boxList.length;
            const pages = Math.ceil(totalBoxes / boxItemPerPage);
            const boxesToShow = json.boxList.slice(offset, offset + boxItemPerPage);

            boxesToShow.forEach(boxes => {
                let statusText = boxes.qty === 0 ? "Out of stock" : "In Stock";

                let boxItemCard = `
                <div class="col-md-3 col-sm-4" style="padding-top: 8px;" id="BoxItem">
                    <div class="single-new-arrival bg-light">
                        <div class="prod_2i3 clearfix w-100 top-0 text-end"></div>
                        <div class="single-new-arrival-bg">
                            <img src="assets/product-images/giftBox-images/${boxes.boxId}/image1.jpeg"
                                 onclick="window.location.href='singleProduct.html?boxId=${boxes.boxId}&boxName=${boxes.name}'"
                                alt="new-arrivals images" />
                            <div class="new-arrival-cart getTobox"
                                onclick="window.location.href='items.html?boxId=${boxes.boxId}&boxName=${boxes.name}'">
                                <div class="row gx-5">
                                    <div class="col text-center" id="getTheBox">
                                        <p>
                                            <span style="margin-right: 10px;">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14"
                                                    fill="currentColor" style="margin-top: -5px;"
                                                    class="bi bi-cart4" viewBox="0 0 16 16">
                                                    <path
                                                        d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l.5 2H5V5H3.14zM6 5v2h2V5H6zm3 0v2h2V5H9zm3 0v2h1.36l.5-2H12zm1.11 3H12v2h.61l.5-2zM11 8H9v2h2V8zM8 8H6v2h2V8zM5 8H3.89l.5 2H5V8zm0 5a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z" />
                                                </svg>
                                            </span>Get <span>the </span> Box
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="pt-4 pb-4 ps-3 pe-3 mb-2 clearfix" style="overflow: hidden;">
                            <h6 class="mt-2"><a href="#" class="text-dark text-decoration-none"
                                    style="font-size: 14px;">${boxes.name}</a></h6>
                            <h6 style="font-size: 13px; font-weight:560;"><a
                                    class="text-dark text-decoration-none proType" href="#">${statusText}</a></h6>
                            <hr>
                            <h6 class="fw-normal mb-0 pull-right fw-bold col_yell">Rs.
                                ${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(boxes.price)}
                            </h6>
                        </div>
                    </div>
                </div>`;
                boxCard.innerHTML += boxItemCard;
            });

            let pagination_container = document.getElementById("pagination-container");
            pagination_container.innerHTML = "";

            if (currentBoxPage > 0) {
                let prev_button_clone = pegination_buttn.cloneNode(true);
                prev_button_clone.innerHTML = "Prev";
                prev_button_clone.onclick = function (e) {
                    e.preventDefault();
                    currentBoxPage--;
                    loadBoxData(currentBoxPage * boxItemPerPage);
                };
                pagination_container.appendChild(prev_button_clone);
            }

            for (let i = 0; i < pages; i++) {
                let pagination_buttn_clone = pegination_buttn.cloneNode(true);
                pagination_buttn_clone.innerHTML = i + 1;

                pagination_buttn_clone.onclick = function (e) {
                    e.preventDefault();
                    currentBoxPage = i;
                    loadBoxData(currentBoxPage * boxItemPerPage);
                };

                if (i === currentBoxPage) {
                    pagination_buttn_clone.className = "page-link page-link-next";
                    pagination_buttn_clone.style.cssText = `
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
                    pagination_buttn_clone.className = "page-link page-link-next";
                    pagination_buttn_clone.style.cssText = `
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

                pagination_container.appendChild(pagination_buttn_clone);
            }

            if (currentBoxPage < pages - 1) {
                let pagination_next_btn = pegination_buttn.cloneNode(true);
                pagination_next_btn.innerHTML = "Next";
                pagination_next_btn.onclick = function (e) {
                    e.preventDefault();
                    currentBoxPage++;
                    loadBoxData(currentBoxPage * boxItemPerPage);
                };
                pagination_container.appendChild(pagination_next_btn);
            }
        } else {
            notyf.error("No box data found.");
        }
    } else {
        notyf.error("Failed to load box data");
}
}


async function checkSessionBox() {

    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const response = await fetch("CheckSessionBox");

    if (response.ok) {
//       
    } else {
        notyf.error("Something went wrong.");
    }

}