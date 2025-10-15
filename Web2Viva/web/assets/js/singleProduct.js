async function loadData() {
    const notyf = new Notyf({
        duration: 4000,
        position: {x: 'right', y: 'top'},
        ripple: true
    });

    const searchParam = new URLSearchParams(window.location.search);
    if (searchParam.has("id")) {
        const productId = searchParam.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                document.getElementById("img1").src = `assets/product-images/${json.item.id}/image1.jpeg`;
                document.getElementById("img2").src = `assets/product-images/${json.item.id}/image2.jpeg`;
                document.getElementById("img3").src = `assets/product-images/${json.item.id}/image3.jpeg`;
                document.getElementById("img4").src = `assets/product-images/${json.item.id}/image4.jpeg`;

                document.getElementById("img-bg").src = `assets/product-images/${json.item.id}/image1.jpeg`;

                ["img1", "img2", "img3", "img4"].forEach(id => {
                    document.getElementById(id).addEventListener("click", function (e) {
                        e.preventDefault();
                        document.getElementById("img-bg").src = this.src;
                    });
                });

                document.getElementById("item-title").innerHTML = json.item.name;
                document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2}).format(json.item.price);
                document.getElementById("product-description").innerHTML = json.item.description;
                document.getElementById("stock-qty").innerHTML = `${json.item.qty} stock left`;
                document.getElementById("product-category").innerHTML = json.item.category.name;


            } else {
                notyf.error(json.message);
            }
        } else {
            notyf.error(json.message);
        }
    } else if (searchParam.has("boxId")) {
        const boxId = searchParam.get("boxId");
        const response = await fetch("LoadSingleProduct?boxId=" + boxId);

        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                document.getElementById("img1").src = `assets/product-images/giftBox-images/${json.giftBox.boxId}/image1.jpeg`;
                document.getElementById("img2").src = `assets/product-images/giftBox-images/${json.giftBox.boxId}/image2.jpeg`;
                document.getElementById("img3").src = `assets/product-images/giftBox-images/${json.giftBox.boxId}/image3.jpeg`;
                document.getElementById("img4").src = `assets/product-images/giftBox-images/${json.giftBox.boxId}/image4.jpeg`;

                document.getElementById("img-bg").src = `assets/product-images/giftBox-images/${json.giftBox.boxId}/image1.jpeg`;

                ["img1", "img2", "img3", "img4"].forEach(id => {
                    document.getElementById(id).addEventListener("click", function (e) {
                        e.preventDefault();
                        document.getElementById("img-bg").src = this.src;
                    });
                });

                document.getElementById("item-title").innerHTML = json.giftBox.name;
                document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2}).format(json.giftBox.price);
                document.getElementById("product-description").innerHTML = json.giftBox.description;
                document.getElementById("stock-qty").innerHTML = `${json.giftBox.qty} stock left`;
                document.getElementById("product-category").innerHTML = "Gift-Box";


            } else {
                notyf.error(json.message);
            }
        } else {
            notyf.error(json.message);
        }

    }



}



function addToBox() {
    const searchParam = new URLSearchParams(window.location.search);
    const boxId = searchParam.get("boxId");
    const boxName = searchParam.get("boxName");
    console.log(boxId+" "+boxName);
    window.location = "items.html?boxId="+ boxId+"&boxName="+boxName;
}

