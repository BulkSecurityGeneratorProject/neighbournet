(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SiteDetailController', SiteDetailController);

    SiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$http', 'previousState', 'entity', 'Site', 'Document', 'Selector', 'SiteWS'];

    function SiteDetailController($scope, $rootScope, $stateParams, $http, previousState, entity, Site, Document, Selector, SiteWS) {
        var vm = this;

        vm.site = entity;
        vm.previousState = previousState.name;
        vm.stats = {};

        SiteWS.connect();

        var unsubscribe = $rootScope.$on('neighbournetApiApp:siteUpdate', function (event, result) {
            vm.site = result;
        });
        $scope.$on('$destroy', unsubscribe);

        SiteWS.receive().then(null, null, function (stats) {
            console.log(stats);
            vm.stats = stats;
            if (vm.stats['numberVisited'] == vm.stats['total']) {
                $('.js-start-crawl').removeClass('disabled');
            } else {
                $('.js-start-crawl').addClass('disabled');
            }
        });

        $scope.initCrawl = function () {
            $('.js-start-crawl').addClass('disabled');
            console.log('staring crawl for site ' + entity);
            var data = $.param({
                'id': entity.id
            });
            $http({
                method: 'POST',
                url: 'crawler',
                data: data,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
        }.bind(this)
    }
})();
