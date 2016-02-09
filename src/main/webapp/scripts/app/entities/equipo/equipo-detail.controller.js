'use strict';

angular.module('jhipsterApp')
    .controller('EquipoDetailController', function ($scope, $rootScope, $stateParams, entity, Equipo, Jugador, Estadio, Socio, Entrenador, Temporada) {
        $scope.equipo = entity;
        $scope.load = function (id) {
            Equipo.get({id: id}, function(result) {
                $scope.equipo = result;
            });
        };
        $rootScope.$on('jhipsterApp:equipoUpdate', function(event, result) {
            $scope.equipo = result;
        });
    });
