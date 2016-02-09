'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jugador', {
                parent: 'entity',
                url: '/jugadors',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.jugador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugador/jugadors.html',
                        controller: 'JugadorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jugador.detail', {
                parent: 'entity',
                url: '/jugador/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.jugador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugador/jugador-detail.html',
                        controller: 'JugadorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugador');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Jugador', function($stateParams, Jugador) {
                        return Jugador.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jugador.new', {
                parent: 'jugador',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/jugador/jugador-dialog.html',
                        controller: 'JugadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nombre: null, nacimiento: null, puntuacion: null, asistencias: null, rebotes: null, posicion: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('jugador', null, { reload: true });
                    }, function() {
                        $state.go('jugador');
                    })
                }]
            })
            .state('jugador.edit', {
                parent: 'jugador',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/jugador/jugador-dialog.html',
                        controller: 'JugadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Jugador', function(Jugador) {
                                return Jugador.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jugador', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
