function updateTotalPrice() {
    var totalCartPrice = 0; // Khởi tạo tổng tiền

    // Lặp qua tất cả các sản phẩm trong giỏ hàng
    var cartItems = document.querySelectorAll('.shopping-list li');
    cartItems.forEach(function (item) {
        // Lấy số lượng sản phẩm, đảm bảo không nhỏ hơn 1
        var quantity = parseInt(item.querySelector('.quantity').textContent.replace('x', '').trim(), 10);

        // Lấy giá sản phẩm
        var pricePerItem = parseFloat(item.querySelector('.amount').textContent.replace(/[^\d.-]/g, ''));

        // Lấy phần trăm giảm giá nếu có (cho cả device và accessory)
        var discountPercentage = 0;
        var discountElementDevice = item.querySelector('.discount-price-device');
        var discountElementAccessory = item.querySelector('.discount-price-accessory');
        if (discountElementDevice && discountElementDevice.textContent.trim() !== "") {
            discountPercentage = parseFloat(discountElementDevice.textContent.replace('%', '').trim()) || 0;
        } else if (discountElementAccessory && discountElementAccessory.textContent.trim() !== "") {
            discountPercentage = parseFloat(discountElementAccessory.textContent.replace('%', '').trim()) || 0;
        }

        // Tính giá sau khi đã áp dụng giảm giá
        var priceAfterDiscount = pricePerItem * (1 - discountPercentage / 100);

        // Cộng vào tổng tiền (giá đã giảm giá * số lượng)
        totalCartPrice += priceAfterDiscount * quantity;
    });

    // Hiển thị tổng tiền
    document.querySelector(".total-amount").textContent = totalCartPrice.toFixed(2) + 'đ';
}

// Gọi hàm updateTotalPrice khi trang được tải hoàn thành
document.addEventListener('DOMContentLoaded', function() {
    updateTotalPrice();
});
