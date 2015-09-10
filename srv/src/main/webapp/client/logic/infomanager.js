var retryCount = 10;
var userInfo = {};

function getAndMangeUserInfo(){
    if(localStorage.getItem('userInfo')){
        if(!userInfo.common){
            userInfo.common = JSON.parse(localStorage.getItem('userInfo'));
        }
        manageUserInfo();
    }else{
        if(retryCount > 0){
            retryCount--;
            $.ajax({
                type: "GET",
                url: '/rest/user',
                success: function(result) {
                    console.log(result);
                    localStorage.setItem('userInfo', JSON.stringify(result));

                    getAndMangeUserInfo();

                },
                error: function() {
                    getAndMangeUserInfo();
                }
            });
        }else{
            console.warn("problems with User Data");
        }
    }
}

function manageUserInfo(){
    setAvatar();
    setName();
}

function setAvatar(){
    $(".avatar-guy").each(function(){
        $(this).attr('src', userInfo.common.photoUri);
    });
}

function setName(){
    $(".avatar-user-name").each(function(){
        this.innerHTML = userInfo.common.fullName;
    });
}

function i18n(lang){
    if(!userInfo.localeData && !localStorage.getItem('localeData')){
        $.getJSON('logic/lang/' + lang + '.json').done(function (data, response) {
            userInfo.localeData = data;
            localStorage.setItem('localeData', JSON.stringify(data));
            setLocale();
        }) .fail(function( xhr, textStatus, error ) {
            console.warn('language file is missing');
            var err = textStatus + ", " + error;
            console.log( "Request Failed: " + err );
            i18n('eng');
        });
    }else if(localStorage.getItem('localeData')){
        userInfo.localeData = JSON.parse(localStorage.getItem('localeData'));
        setLocale();
    }

}

function setLocale(){
    $.each(userInfo.localeData, function(index, value){
        $(index).each(function(){
            this.innerHTML = value;
        });
    });
}
