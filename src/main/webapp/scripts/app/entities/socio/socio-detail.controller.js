'use strict';

angular.module('jhipsterApp')
    .controller('SocioDetailController', function ($scope, $rootScope, $stateParams, entity, Socio, Equipo) {
        $scope.socio = entity;
        $scope.load = function (id) {
            Socio.get({id: id}, function(result) {
                $scope.socio = result;
            });
        };
        $rootScope.$on('jhipsterApp:socioUpdate', function(event, result) {
            $scope.socio = result;
        });
    });
