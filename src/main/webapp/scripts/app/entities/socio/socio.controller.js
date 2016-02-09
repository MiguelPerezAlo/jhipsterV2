'use strict';

angular.module('jhipsterApp')
    .controller('SocioController', function ($scope, Socio) {
        $scope.socios = [];
        $scope.loadAll = function() {
            Socio.query(function(result) {
               $scope.socios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Socio.get({id: id}, function(result) {
                $scope.socio = result;
                $('#deleteSocioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Socio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSocioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.socio = {nombre: null, nacimiento: null, tipo: null, cuota: null, id: null};
        };
    });
