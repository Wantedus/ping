$(function () {
        $("#checkgab").click(function () {
            if ($(this).is(":checked")) {
                $("#priorite").show();
            } else {
                $("#priorite").hide();
            }
        });
    });


   $(function() {
   $("input[name='gridRadio2']").click(function() {
     if ($("#gridRadios2").is(":checked")) {
       $("#cacherbulle").show();
     } else {
       $("#cacherbulle").hide();
     }
   });
 });

   $(function () {
    $(":checkbox").click(function (){
      if($("#cmb").is(":checked")&& $("#cmcc").is(":checked")&&$("#cmso").is(":checked")&& $("#bpe").is(":checked")&& $("#abp").is(":checked"))  {
          $("#cible").hide();
        } else if ($("#cmb").is(":checked")|| $("#cmcc").is(":checked")|| $("#cmso").is(":checked")|| $("#bpe").is(":checked") || $("#abp").is(":checked")){
              $("#cible").show();
        } else {
          $("#cible").hide();
        }
      });
    });

     $(function () {
        $("#agence").click(function () {
            if ($(this).is(":checked")) {
                $("#agencearea").show();
            } else {
                $("#agencearea").hide();
            }
        });
    });

    $(function () {
        $("#clients").click(function () {
            if ($(this).is(":checked")) {
                $("#listeclient").show();
            } else {
                $("#listeclient").hide();
            }
        });
    });