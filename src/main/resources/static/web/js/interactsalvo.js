(function() {

    function init() {
        var startPos = null;

        interact('.block').draggable({
            snap: {
                targets: [startPos],
                range: Infinity,
                relativePoints: [ { x: 0.5, y: 0.5 } ],
                endOnly: true
            },
            onstart: function (event) {
                var rect = interact.getElementRect(event.target);

                // record center point when starting the very first a drag
                startPos = {
                    x: rect.left + rect.width  / 2,
                    y: rect.top  + rect.height / 2
                }

                event.interactable.draggable({
                    snap: {
                        targets: [startPos]
                    }
                });
            },
            // call this function on every dragmove event
            onmove: function (event) {
                var target = event.target,
                    // keep the dragged position in the data-x/data-y attributes
                    x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
                    y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                // translate the element
                target.style.webkitTransform =
                    target.style.transform =
                        'translate(' + x + 'px, ' + y + 'px)';

                // update the posiion attributes
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
                target.classList.add('getting--dragged');
            },
            onend: function (event) {
                event.target.classList.remove('getting--dragged')
            }
        });

       // interact('.droppable:not(.caught--it)').dropzone({
        interact('.dropzone').dropzone({
            accept: '.block',
            overlap: .5,

            ondropactivate: function (event) {
                event.target.classList.add('can--drop');
            },
            ondragenter: function (event) {
                var draggableElement = event.relatedTarget,
                    dropzoneElement  = event.target,
                    dropRect         = interact.getElementRect(dropzoneElement),
                    dropCenter       = {
                        x: dropRect.left + dropRect.width  / 2,
                        y: dropRect.top  + dropRect.height / 2
                    };

                event.draggable.draggable({
                    snap: {
                        targets: [dropCenter]
                    }
                });

                // feedback the possibility of a drop
                dropzoneElement.classList.add('can--catch');
                draggableElement.classList.add('drop--me');
            },
            ondragleave: function (event) {
                // remove the drop feedback style
                event.target.classList.remove('can--catch', 'caught--it');
                event.relatedTarget.classList.remove('drop--me');
            },
            ondrop: function (event) {
                console.log("Index of dropped node: " + getNodeIndex(event.target));
                console.log("Index of dragged node: " + getNodeIndex(event.relatedTarget.parentNode));
                //event.relatedTarget.textContent = 'Dropped';
                console.log("Dropped!");
                console.log("related target: " + event.relatedTarget.parentNode);
                console.log(event.draggable);
                event.target.classList.add('caught--it');
            },
            ondropdeactivate: function (event) {
                // remove active dropzone feedback
                event.target.classList.remove('can--drop');
                event.target.classList.remove('can--catch');
            }
        });
    }

    function getNodeIndex(node) {
        var index = 0;
        while ( (node = node.previousSibling) ) {
            if (node.nodeType != 3 || !/^\s*$/.test(node.data)) {
                index++;
            }
        }
        return index;
    }

    function eleHasClass(el, cls) {
        return el.className && new RegExp("(\\s|^)" + cls + "(\\s|$)").test(el.className);
    }

    window.onload = function() {
        init();
    }

})();



///////////////////

/*
var startPos = null;

interact('.block')
    .draggable({
        onmove: function (event) {
            var target = event.target,
                x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
                y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

            target.style.webkitTransform =
                target.style.transform =
                    'translate(' + x + 'px, ' + y + 'px)';

            target.setAttribute('data-x', x);
            target.setAttribute('data-y', y);
        },
        onend: function (event) {
            var textEl = event.target.querySelector('p');

            textEl && (textEl.textContent =
                'moved a distance of '
                + (Math.sqrt(event.dx * event.dx +
                event.dy * event.dy)|0) + 'px');
        }
    })
    .inertia(true)
    .restrict({
        drag: "",
        endOnly: true,
        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
    })
    .snap({
        mode: 'anchor',
        anchors: [],
        range: Infinity,
        elementOrigin: { x: 0.5, y: 0.5 },
        endOnly: true
    })
    .on('dragstart', function (event) {
         if (!startPos) {
            var rect = interact.getElementRect(event.target);

            // record center point when starting the very first a drag
            startPos = {
                x: rect.left + rect.width  / 2,
                y: rect.top  + rect.height / 2
            }
        }

        // snap to the start position
        event.interactable.snap({ anchors: [startPos] });
    });


interact('.dropzone')
// enable draggables to be dropped into this
    .dropzone({ overlap: 'center' })
    // only accept elements matching this CSS selector
    .accept('.block')
    // listen for drop related events
    .on('dragenter', function (event) {
        var dropRect = interact.getElementRect(event.target),
            dropCenter = {
                x: dropRect.left + dropRect.width  / 2,
                y: dropRect.top  + dropRect.height / 2
            };

        event.draggable.snap({
            anchors: [ dropCenter ]
        });

        var draggableElement = event.relatedTarget,
            dropzoneElement = event.target;

        // feedback the possibility of a drop
        dropzoneElement.classList.add('drop-target');
        draggableElement.classList.add('can-drop');
    })
    .on('dragleave', function (event) {
        event.draggable.snap(false);

        // when leaving a dropzone, snap to the start position
        event.draggable.snap({ anchors: [startPos] });

        // remove the drop feedback style
        event.target.classList.remove('drop-target');
        event.relatedTarget.classList.remove('can-drop');
        event.target.removeAttribute( "data-test" );
    })
    .on('dropactivate', function (event) {
        // add active dropzone feedback
        event.target.classList.add('drop-active');
    })
    .on('dropdeactivate', function (event) {
        // remove active dropzone feedback
        event.target.classList.remove('drop-active');
        event.target.classList.remove('drop-target');
    })
    .on('drop', function (event) {
        event.relatedTarget.textContent = '';
        var el = event.target;
        $(el).attr("data-salvoshot","salvo"); //setter

        // var a = $('#mydiv').data('myval'); //getter
        // $('#mydiv').attr("data-myval","20"); //setter

    });
*/

/*.on('dragenter', function (event) {
    var draggableElement = event.relatedTarget,
        dropzoneElement = event.target;

    // feedback the possibility of a drop
    dropzoneElement.classList.add('drop-target');
    draggableElement.classList.add('can-drop');
})
.on('dragleave', function (event) {
    // remove the drop feedback style
    event.target.classList.remove('drop-target');
    event.relatedTarget.classList.remove('can-drop');
})
.on('dropactivate', function (event) {
    // add active dropzone feedback
    event.target.classList.add('drop-active');
})
.on('dropdeactivate', function (event) {
    // remove active dropzone feedback
    event.target.classList.remove('drop-active');
    event.target.classList.remove('drop-target');
})
.on('drop', function (event) {
    event.relatedTarget.textContent = '';
});*/







// // target elements with the "draggable" class
// interact('.draggable')
//     .draggable({
//         // enable inertial throwing
//         inertia: true,
//         // keep the element within the area of it's parent
//         restrict: {
//             restriction: "parent",
//             endOnly: true,
//             elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
//         },
//         // enable autoScroll
//         autoScroll: true,
//
//         // call this function on every dragmove event
//         onmove: dragMoveListener,
//         // call this function on every dragend event
//         onend: function (event) {
//             var textEl = event.target.querySelector('p');
//
//             textEl && (textEl.textContent =
//                 'moved a distance of '
//                 + (Math.sqrt(Math.pow(event.pageX - event.x0, 2) +
//                 Math.pow(event.pageY - event.y0, 2) | 0))
//                     .toFixed(2) + 'px');
//         }
//     });
//
// function dragMoveListener (event) {
//     var target = event.target,
//         // keep the dragged position in the data-x/data-y attributes
//         x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
//         y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
//
//     // translate the element
//     target.style.webkitTransform =
//         target.style.transform =
//             'translate(' + x + 'px, ' + y + 'px)';
//
//     // update the posiion attributes
//     target.setAttribute('data-x', x);
//     target.setAttribute('data-y', y);
// }
//
// // this is used later in the resizing and gesture demos
// window.dragMoveListener = dragMoveListener;
//
//
//
//
// /* The dragging code for '.draggable' from the demo above
//  * applies to this demo as well so it doesn't have to be repeated. */
//
// // enable draggables to be dropped into this
// interact('.dropzone').dropzone({
//     // only accept elements matching this CSS selector
//     accept: '#yes-drop',
//     // Require a 75% element overlap for a drop to be possible
//     overlap: 0.75,
//
//     // listen for drop related events:
//
//     ondropactivate: function (event) {
//         // add active dropzone feedback
//         event.target.classList.add('drop-active');
//     },
//     ondragenter: function (event) {
//         var draggableElement = event.relatedTarget,
//             dropzoneElement = event.target;
//
//         // feedback the possibility of a drop
//         dropzoneElement.classList.add('drop-target');
//         draggableElement.classList.add('can-drop');
//         draggableElement.textContent = 'Dragged in';
//     },
//     ondragleave: function (event) {
//         // remove the drop feedback style
//         event.target.classList.remove('drop-target');
//         event.relatedTarget.classList.remove('can-drop');
//         event.relatedTarget.textContent = 'Dragged out';
//     },
//     ondrop: function (event) {
//         event.relatedTarget.textContent = 'Dropped';
//         console.log(event.target);
//     },
//     ondropdeactivate: function (event) {
//         // remove active dropzone feedback
//         event.target.classList.remove('drop-active');
//         event.target.classList.remove('drop-target');
//     }
// });