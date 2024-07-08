//Thời Gian
function time() {
    var today = new Date();
    var weekday = new Array(7);
    weekday[0] = "Chủ Nhật";
    weekday[1] = "Thứ Hai";
    weekday[2] = "Thứ Ba";
    weekday[3] = "Thứ Tư";
    weekday[4] = "Thứ Năm";
    weekday[5] = "Thứ Sáu";
    weekday[6] = "Thứ Bảy";
    var day = weekday[today.getDay()];
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    nowTime = h + " giờ " + m + " phút " + s + " giây";
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    today = day + ', ' + dd + '/' + mm + '/' + yyyy;
    tmp = '<span class="date"> ' + today + ' - ' + nowTime +
        '</span>';
    document.getElementById("clock").innerHTML = tmp;
    clocktime = setTimeout("time()", "1000", "Javascript");

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }
}

function deletionNotice(element, button, url) {
    if (element.className === 'swal-overlay') {
        element.className = 'swal-overlay swal-overlay--show-modal';
    } else {
        element.className = 'swal-overlay';
    }
    button.addEventListener('click', function () {
        window.location = url;
    });
}

// active menu
var menu = document.getElementsByClassName('app-menu')[0];
var menuLinks = document.querySelectorAll('.app-menu__item');
menuLinks.forEach(link => {
    link.addEventListener('click', function (event) {
        event.preventDefault();
        var turnOfActive = document.getElementsByClassName('app-menu__item active')[0];
        sessionStorage.setItem('turnOfActive', turnOfActive.getAttribute('id'));
        sessionStorage.setItem('activeTab', this.getAttribute('id'));
        window.location = this.getAttribute('href');
    });
});

document.addEventListener('DOMContentLoaded', function () {
    var turnOfActive = sessionStorage.getItem('turnOfActive');
    var activeTab = sessionStorage.getItem('activeTab');

    if (activeTab !== null && turnOfActive !== null) {

        var offActive = document.getElementById(turnOfActive);
        var active = document.getElementById(activeTab);

        if (activeTab === 'device' || activeTab === 'acc') {
            active = document.getElementById('product');
        }

        offActive.classList.remove('active');
        active.classList.add('active');
    } else {
        document.getElementById('dashboard').classList.add('active');
    }

});

function validateText(input, mess, title) {
    var inputValue = input.value;
    var resultElement = document.getElementById(mess);
    if (inputValue.trim() === '') {
        resultElement.textContent = title + " không được để trống";
        resultElement.style.color = "red";
        input.value = "";
    } else {
        resultElement.textContent = "";
    }
}

function validateQuantity(input) {
    var inputValue = input.value;
    var resultElement = document.getElementById("messQuantity");
    if (parseInt(inputValue) <= 0) {
        resultElement.textContent = "Vui lòng nhập vào giá trị số nguyên lớn hơn 0";
        resultElement.style.color = "red";
        input.value = "";
    } else {
        resultElement.textContent = "";
    }
}

function validateInput(input) {
    var inputValue = input.value;
    var resultElement = document.getElementById("mess");
    if (isNaN(inputValue) || parseFloat(inputValue) < 1000) {
        resultElement.textContent = "Vui lòng nhập vào giá tiền lớn hơn hoặc bằng 1000";
        resultElement.style.color = "red";
        input.value = "";
    } else {
        resultElement.textContent = "";
    }
}

function validateDiscount(input) {
    var inputValue = input.value;
    var resultElement = document.getElementById("messDiscount");
    if (parseInt(inputValue) < 0 || parseInt(inputValue) > 100) {
        resultElement.textContent = "Vui lòng nhập vào giá trị số nguyên > 0 và < 100";
        resultElement.style.color = "red";
        input.value = "";
    } else {
        resultElement.textContent = "";
    }
}

function validateStartDate() {
    var start = document.getElementById('startDate').value;
    var end = document.getElementById('endDate').value;

    var currentDate = new Date();
    var startDate = new Date(start);
    var endDate = new Date(end);
    if (startDate < currentDate) {
        alert('Ngày bắt đầu phải lớn hơn ngày hiện tại');
        document.getElementById('startDate').value = '';
    }
    if (startDate >= endDate) {
        alert('Ngày bắt đầu phải nhỏ hơn ngày kết thúc');
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
    }
}

function validateEndDate() {
    var start = document.getElementById('startDate').value;
    var end = document.getElementById('endDate').value;

    var startDate = new Date(start);
    var endDate = new Date(end);
    if (startDate >= endDate) {
        alert('Ngày bắt đầu phải nhỏ hơn ngày kết thúc');
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
    }

}

function mainIm(input) {
    const file = input.files[0];
    const preview = document.getElementById('mainPreview');
    var fileName = file.name;
    var extension = fileName.split('.').pop().toLowerCase();

    if (extension !== 'jpg' && extension !== 'jpeg' && extension !== '.png') {
        alert('Vui lòng chọn tệp có đuôi .jpg hoặc .jpeg hoặc .png');
        input.value = '';
        preview.innerHTML = '';
        return;
    }

    const fileSizeInMB = file.size / (1024 * 1024);
    if (fileSizeInMB > 5) {
        alert('Dung lượng tệp vượt quá 5MB. Vui lòng chọn tệp khác.');
        input.value = '';
        preview.innerHTML = '';
        return;
    }

    preview.innerHTML = '';
    const reader = new FileReader();
    reader.onload = function (e) {
        const img = document.createElement('img');
        img.src = e.target.result;
        preview.appendChild(img);
    };
    reader.readAsDataURL(file);
}

function extraIm(input) {
    const selectedFiles = [];
    const files = input.files;
    const preview = document.getElementById('preview');
    let totalFileSizeInMB = 0;

    Array.from(files).forEach((file) => {
        var fileName = file.name;
        var extension = fileName.split('.').pop().toLowerCase();

        if (extension !== 'jpg' && extension !== 'jpeg' && extension !== 'png') {
            alert('Vui lòng chọn tệp có đuôi .jpg hoặc .jpeg hoặc .png');
            input.value = '';
            preview.innerHTML = '';
            return;
        }

        const fileSizeInMB = file.size / (1024 * 1024);
        totalFileSizeInMB += fileSizeInMB;

        if (fileSizeInMB > 5) {
            alert('Dung lượng tệp vượt quá 5MB. Vui lòng chọn tệp khác.');
            input.value = '';
            preview.innerHTML = '';
            return;
        }

        if (totalFileSizeInMB > 20) {
            alert('Tổng dung lượng tệp vượt quá 20MB. Vui lòng chọn tệp khác.');
            input.value = '';
            preview.innerHTML = '';
            return;
        }
    });

    preview.innerHTML = '';

    Array.from(files).forEach((file) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
}