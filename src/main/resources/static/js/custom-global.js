'use strict';

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