const EventEmitter = require('events');
const utils = require('util');
function Finder() {
    EventEmitter.call(this)
}
utils.inherits(Finder, EventEmitter);
var finder = new Finder();
finder.on('success', (data) => {
    console.log(data);
});
finder.emit('success', 1);
