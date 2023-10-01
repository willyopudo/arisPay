'use strict';
function validate(file) {
    var ext = file.split(".");
    ext = ext[ext.length-1].toLowerCase();
    var arrayExtensions = ["jpg" , "jpeg", "png", "bmp", "gif"];

    if (arrayExtensions.lastIndexOf(ext) == -1) {
        Swal.fire({
          title: 'Failure!',
          text: 'Wrong file type selected',
          icon: 'warning',
          confirmButtonText: 'Cancel'
        })
        $("#mediafile").val("");
    }
}
(function () {
	$('#statustoggle').change(function() {
        if(this.checked) {
            Swal.fire({
              title: 'Success!',
              text: 'You checked the toggle',
              icon: 'info',
              confirmButtonText: 'Ok'
            })
            $('input[name=status]').val(1);
        }
        if(!this.checked){
            Swal.fire({
              title: 'Success!',
              text: 'You unchecked the toggle',
              icon: 'warning',
              confirmButtonText: 'Ok'
            })

            $('input[name=status]').val(0);
        }
        //$('#textbox1').val(this.checked);
    });

})();