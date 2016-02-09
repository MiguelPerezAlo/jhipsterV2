'use strict';

angular.module('jhipsterApp')
    .controller('TemporadaController', function ($scope, Temporada) {
        $scope.temporadas = [];
        $scope.loadAll = function() {
            Temporada.query(function(result) {
               $scope.temporadas = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
                $('#deleteTemporadaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Temporada.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTemporadaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.temporada = {year: null, nombre: null, id: null};
        };
    });
