'use strict';

angular.module('jhipsterApp').controller('EntrenadorDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Entrenador', 'Equipo',
        function($scope, $stateParams, $modalInstance, $q, entity, Entrenador, Equipo) {

        $scope.entrenador = entity;
        $scope.equipos = Equipo.query({filter: 'entrenador-is-null'});
        $q.all([$scope.entrenador.$promise, $scope.equipos.$promise]).then(function() {
            if (!$scope.entrenador.equipo.id) {
                return $q.reject();
            }
            return Equipo.get({id : $scope.entrenador.equipo.id}).$promise;
        }).then(function(equipo) {
            $scope.equipos.push(equipo);
        });
        $scope.load = function(id) {
            Entrenador.get({id : id}, function(result) {
                $scope.entrenador = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:entrenadorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.entrenador.id != null) {
                Entrenador.update($scope.entrenador, onSaveFinished);
            } else {
                Entrenador.save($scope.entrenador, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
