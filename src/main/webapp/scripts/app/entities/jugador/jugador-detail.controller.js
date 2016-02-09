'use strict';

angular.module('jhipsterApp')
    .controller('JugadorDetailController', function ($scope, $rootScope, $stateParams, entity, Jugador, Equipo) {
        $scope.jugador = entity;
        $scope.load = function (id) {
            Jugador.get({id: id}, function(result) {
                $scope.jugador = result;
            });
        };
        $rootScope.$on('jhipsterApp:jugadorUpdate', function(event, result) {
            $scope.jugador = result;
        });
    });
