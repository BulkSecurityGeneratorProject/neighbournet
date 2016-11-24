(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('feed-item', {
                parent: 'entity',
                url: '/feed-item?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FeedItems'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/feed-item/feed-items.html',
                        controller: 'FeedItemController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                }
            })
            .state('feed-item-detail', {
                parent: 'entity',
                url: '/feed-item/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FeedItem'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/feed-item/feed-item-detail.html',
                        controller: 'FeedItemDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'FeedItem', function ($stateParams, FeedItem) {
                        return FeedItem.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'feed-item',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('feed-item-detail.edit', {
                parent: 'feed-item-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/feed-item/feed-item-dialog.html',
                        controller: 'FeedItemDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['FeedItem', function (FeedItem) {
                                return FeedItem.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('feed-item.new', {
                parent: 'feed-item',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/feed-item/feed-item-dialog.html',
                        controller: 'FeedItemDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('feed-item', null, {reload: 'feed-item'});
                    }, function () {
                        $state.go('feed-item');
                    });
                }]
            })
            .state('feed-item.edit', {
                parent: 'feed-item',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/feed-item/feed-item-dialog.html',
                        controller: 'FeedItemDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['FeedItem', function (FeedItem) {
                                return FeedItem.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('feed-item', null, {reload: 'feed-item'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('feed-item.delete', {
                parent: 'feed-item',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/feed-item/feed-item-delete-dialog.html',
                        controller: 'FeedItemDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['FeedItem', function (FeedItem) {
                                return FeedItem.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('feed-item', null, {reload: 'feed-item'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
