var page = require('webpage').create();
var page2 = require('webpage').create();
var url = 'http://www.infoq.com';

page.onConsoleMessage = function (msg) {
  // Phantom.js does not display console messages by default
  console.log(msg);
};

page.onAlert = function (msg) {
  console.log(msg);
};

page.onError = function (msg, trace) {
  // Ignore errors
  console.error(msg);
};


var login_url = 'https://www.infoq.com/login.action';
var login_data = 'username=uwe@dauernheim.net&password=********';

page.open(login_url, 'post', login_data, function (status) {
  if (status !== 'success') {
    console.error('Unable to login!');
  } else {
    console.log(page.content);
  }
  phantom.exit();
});

/*
page.open(url, function (status) {
  if (status !== 'success') {
    console.log('Failed to load main page!');
    phantom.exit(1);
  }
  
  console.log('main page opened');
  
  // page.injectJs('zepto.min.js');
  
  page.evaluate(function () {
    document.querySelector('#login_username').value = 'uwe@dauernheim.net';
    document.querySelector('#login_password').value = '05I08l81K';
    document.querySelector('#login_formLogin').submit();
  });
  window.setTimeout(function () {
    var rss_url = page.evaluate(function () {
      // return $('#headerRssLink').attr('href');
      //return document.getElementById('headerRssLink').href;
      console.log(document.getElementById('headerRssLink').href);
      return document.getElementById('logout').href;
    });
    
    console.log(rss_url);
    
    phantom.exit();
  }, 10000);
  
  
  /*
    page2.open(url + rss_url, function (status) {
    if (status !== 'success') {
      console.error('Failed to load rss url!');
      phantom.exit(1);
    }
    
    console.log('rss url opened');
    
    
    phantom.exit();
    });
  */
/*
});
*/
