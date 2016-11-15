(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('site', {
                parent: 'entity',
                url: '/site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sites'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/site/sites.html',
                        controller: 'SiteController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {}
            })
            .state('site-detail', {
                parent: 'entity',
                url: '/site/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Site'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/site/site-detail.html',
                        controller: 'SiteDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Site', function ($stateParams, Site) {
                        return Site.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'site',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('site-detail.edit', {
                parent: 'site-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/site/site-dialog.html',
                        controller: 'SiteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Site', function (Site) {
                                return Site.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('site.new', {
                parent: 'site',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/site/site-dialog.html',
                        controller: 'SiteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    regex: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('site', null, {reload: 'site'});
                    }, function () {
                        $state.go('site');
                    });
                }]
            })
            .state('site.edit', {
                parent: 'site',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/site/site-dialog.html',
                        controller: 'SiteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Site', function (Site) {
                                return Site.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('site', null, {reload: 'site'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('site.delete', {
                parent: 'site',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/site/site-delete-dialog.html',
                        controller: 'SiteDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Site', function (Site) {
                                return Site.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('site', null, {reload: 'site'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
