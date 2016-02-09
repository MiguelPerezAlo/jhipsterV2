'use strict';

angular.module('jhipsterApp')
    .controller('TemporadaDetailController', function ($scope, $rootScope, $stateParams, entity, Temporada, Equipo) {
        $scope.temporada = entity;
        $scope.load = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
            });
        };
        $rootScope.$on('jhipsterApp:temporadaUpdate', function(event, result) {
            $scope.temporada = result;
        });
    });
