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
$(document).ready(function() {


});
function deleteRecord(elem) {
	alert("clicked");
    //var confirmMessageBox = confirm('Are you sure you wish to delete this job ?');
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/user/delete/' + elem.id,
                type: "Delete",
                data: {'Id': elem.id},
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    console.log(data);
                    Swal.fire(
                        'Deleted!',
                        'User has been deleted.',
                        'success'
                    ).then(function () {
                        location.reload();
                    });
                },
                error: function (xhr, ajaxOptions, thrownError) {
                console.log(thrownError);
                    Swal.fire(
                        'Cancelled',
                        'Delete Is Failed',
                        'error'
                    );
                }
            });

        }
    });
    //console.log(this.id);
};
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