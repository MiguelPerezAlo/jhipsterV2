'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('temporada', {
                parent: 'entity',
                url: '/temporadas',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.temporada.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temporada/temporadas.html',
                        controller: 'TemporadaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temporada');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('temporada.detail', {
                parent: 'entity',
                url: '/temporada/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.temporada.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temporada/temporada-detail.html',
                        controller: 'TemporadaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temporada');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Temporada', function($stateParams, Temporada) {
                        return Temporada.get({id : $stateParams.id});
                    }]
                }
            })
            .state('temporada.new', {
                parent: 'temporada',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/temporada/temporada-dialog.html',
                        controller: 'TemporadaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {year: null, nombre: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('temporada', null, { reload: true });
                    }, function() {
                        $state.go('temporada');
                    })
                }]
            })
            .state('temporada.edit', {
                parent: 'temporada',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/temporada/temporada-dialog.html',
                        controller: 'TemporadaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Temporada', function(Temporada) {
                                return Temporada.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('temporada', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
