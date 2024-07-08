function updateTotalPriceCart() {
    var totalCartPrice = 0; // Initialize total price
    var selectedCartIds = []; // Initialize array to hold selected cart IDs
    var cartData = [];

    var quantityInputs = document.querySelectorAll('.cart-single-list .row .quantity-input');
    quantityInputs.forEach(function (input) {
        var row = input.closest('.row'); // Get parent element with class '.row'
        var checkbox = row.querySelector('.checkbox-cart'); // Find the corresponding checkbox in this row

        if (checkbox && checkbox.checked) {
            selectedCartIds.push(checkbox.value);
            var cartId = checkbox.value;

            var quantity = Math.max(parseFloat(input.value), 1);
            cartData.push({ id: cartId, quantity: quantity });
            var pricePerItem = 0;
            var discountPercentage = 0;

            if (row.querySelector('.product-price')) {
                pricePerItem = parseFloat(row.querySelector('.product-price').textContent.replace(/[^\d.-]/g, '')); // Get product price
            }

            if (row.querySelector('.product-discount')) {
                discountPercentage = parseFloat(row.querySelector('.product-discount').textContent.replace('%', '')) || 0; // Get discount percentage if any
            }

            // Calculate price after discount
            var priceAfterDiscount = pricePerItem * (1 - discountPercentage / 100);

            // Add to total price (price after discount * quantity)
            totalCartPrice += priceAfterDiscount * quantity;
        }
    });


    document.getElementById("cartSubtotal").textContent = totalCartPrice.toFixed(2) + 'đ';


    document.getElementById('cartData').value = JSON.stringify(cartData);
    document.getElementById('selectedCartIds').value = selectedCartIds.join(',');
    document.getElementById('totalPrice').value = totalCartPrice.toFixed(0);
}

function selectAllCheckboxes(checkbox) {
    var checkboxes = document.getElementsByClassName('checkbox-cart');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = checkbox.checked;
    }
    updateTotalPriceCart(); // Update total price when all checkboxes are selected/deselected
}

// Call updateTotalPrice when the page loads and whenever product quantity changes
document.addEventListener('DOMContentLoaded', function() {
    updateTotalPriceCart();

    var quantityInputs = document.querySelectorAll('.cart-single-list .row .quantity-input');
    quantityInputs.forEach(function(input) {
        input.addEventListener('input', function() {
            if (parseFloat(this.value) < 1) {
                this.value = 1; // Ensure quantity is not less than 1
            }
            updateTotalPriceCart();
        });
    });

    var checkboxes = document.querySelectorAll('.checkbox-cart');
    checkboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            updateTotalPriceCart();
        });
    });

    var checkoutButton = document.getElementById('checkoutButton');
    checkoutButton.addEventListener('click', function(event) {
        event.preventDefault();


        var selectedCartIds = document.getElementById('selectedCartIds').value;
        if (selectedCartIds.length === 0) {

            Swal.fire({
                icon: 'warning',
                title: 'Bạn chưa chọn sản phẩm nào',
                text: 'Hãy chọn ít nhất một sản phẩm để thanh toán',
                confirmButtonText: 'OK'
            });
        } else {

            document.getElementById('checkoutForm').submit();
        }
    });
});
