var Chimera = require('chimera').Chimera;

var myUsername = "my_username";
var myPassword = "my_password";

var c = new Chimera();
c.perform({
  url: "http://www.mywebsite.com",
  locals: {
    username: myUsername,
    password: myPassword
  },
  run: function(callback) {
    // find the form fields and press submit
    pos = jQuery('#login-button').offset()
    window.chimera.sendEvent("click", pos.left + 10, pos.top + 10)
  },
  callback: function(err, result) {
    // capture a screen shot
    c.capture("screenshot.png");

    // save the cookies and close out the browser session
    var cookies = c.cookies();
    c.close();

    // Create a new browser session with cookies from the previous session
    var c2 = new Chimera({
      cookies: cookies
    });
    c2.perform({
      url: "http://www.mywebsite.com",
      run: function(callback) {
        // You're logged in here!
      },
      callback: function(err, result) {
        // capture a screen shot that shows we're logged in
        c2.capture("screenshot_logged_in.png");
        c2.close();
      }
    });
  }
});
