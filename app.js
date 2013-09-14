var page = require('webpage').create();

page.onConsoleMessage = function (msg) {
  // Phantom.js does not display console messages by default
  console.log(msg);
};

page.onError = function (msg, trace) {
  console.error(msg);
  phantom.exit(1);
};

var base_url = 'http://www.infoq.com';
var presentation_url = base_url + '/presentations/';
var presentation_page_url = presentation_url + '0';
var user_agent = 'Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) '
  + 'AppleWebKit/536.26 (KHTML, like Gecko) '
  + 'Version/6.0 Mobile/10A403 Safari/8536.25';

page.open(presentation_page_url, function (status) {
  if (status !== 'success') {
    console.log('Failed to load main page!');
    phantom.exit(1);
  }
  
  // page.injectJs('zepto.min.js');
  var presentations = page.evaluate(function () {
    return $('.news_type_video').map(function (_, p) {
      return {
        'id'     : null,
        'url'    : $('a', p)[0].href,
        'title'  : $('a', p).attr('title'),
        'poster' : $('a img', p)[0].src,
        'length' : $('.videolength', p).text(),
        'author' : $('.author a', p).attr('title').trim(),
        'date'   : Date($('.author', p).text().match(/[a-z]{3} \d{2}, \d{4}/i)),
        'summary': $('p', p).text().trim(),
        'video'  : null,
        'slides' : null
      };
    });
  });
  
  for (var i=0; i < presentations.length; i++) {
    for (var k in presentations[i])
      console.log(k + '\t=>\t' + presentations[i][k]);
    console.log('');
  }
  
  phantom.exit();
});
