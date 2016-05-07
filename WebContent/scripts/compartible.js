// Hack for IE8
if (!Date.now) {
    Date.now = function() { return new Date().getTime(); }
}