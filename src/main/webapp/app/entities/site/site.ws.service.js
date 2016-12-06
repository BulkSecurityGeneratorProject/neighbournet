(function () {
    'use strict';
    angular
        .module('neighbournetApiApp')
        .factory('SiteWS', SiteWS);

    SiteWS.$inject = ['$q', '$window'];

    function SiteWS($q, $window) {
        var stompClient = null;
        var subscriber = null;
        var connected = $q.defer();
        var listener = $q.defer();

        var result = {
            receive: receive,
            connect: connect,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return result;

        function receive() {
            return listener.promise;
        }

        function connect() {
            //building absolute path so that websocket doesnt fail when deploying with a context path
            var loc = $window.location;
            var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
            var socket = new SockJS(url);
            stompClient = Stomp.over(socket);
            stompClient.debug = null;
            stompClient.connect({}, function () {
                connected.resolve('success');
            });
        }

        function subscribe() {
            connected.promise.then(function () {
                subscriber = stompClient.subscribe('/topic/crawlstat', function (data) {
                    listener.notify(angular.fromJson(data.body));
                });
            }, null, null);
        }

        function unsubscribe() {
            if (subscriber !== null) {
                subscriber.unsubscribe();
            }
            listener = $q.defer();
        }
    }
})();
