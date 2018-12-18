var exec = require('cordova/exec');

function plugin() {

}

plugin.prototype.startActivityForResult = function (activity, b64Image, picker, success, error) {
    exec(success, error, 'Intents', 'startActivityForResult', [activity, b64Image, picker]);
};

module.exports = new plugin();
