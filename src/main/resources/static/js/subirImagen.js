var fileUpload = document.getElementById('file-upload');
fileUpload.onchange = function (e) {
	var file = $('#file-upload').prop('files');
	readImage(file);
}


function readImage(input) {
	var data = new FormData();
	data.append('file', input[0]);
	var reader = new FileReader();
	reader.onload = function (e) {
	var img = document.getElementById('imcargar');
	img.src= e.target.result;
	img.style.width = "12rem";
	img.style.heigth= "auto";
	var url = document.getElementById('agregarUrl');
	url.value = e.target.result;
	var cambio = document.getElementById('cambioUrl');
	cambio.value = true;
	}
	reader.readAsDataURL(input[0]);
}

/*
function readImage(input) {
	var data = new FormData();
	data.append('file', input[0]);
	$.ajax({
		type: "POST",
        enctype: 'multipart/form-data',
        url: "/subirImagen",
        data: data,
        processData: false,
        contentType: false,
        success: function(res) {
			  var reader = new FileReader();
			  reader.onload = function (e) {
				  	var img = document.getElementById('imcargar');
					img.src= e.target.result;
					img.style.width = "12rem";
					img.style.heigth= "auto";
					//img.className = 
					var url = document.getElementById('agregarUrl');
					url.value = res;
			  }
			  reader.readAsDataURL(input[0]);
		},
		error: function( jqXHR, textStatus, errorThrown ) {
	        console.log(jqXHR);
	        console.log(textStatus);
	        console.log(errorThrown);
	    }
    });
}
*/