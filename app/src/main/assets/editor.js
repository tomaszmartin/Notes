var editor = (function() {

    var instance = {};
    instance.selection = {
        startContainer: 0,
        startOffset: 0,
        endContainer: 0,
        endOffset: 0
    };

    instance.setHtml = function() {
    }

    document.addEventListener('selectionchange', function() {instance..saveRange()});

})();