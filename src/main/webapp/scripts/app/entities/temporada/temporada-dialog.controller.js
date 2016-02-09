'use strict';

angular.module('jhipsterApp').controller('TemporadaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Temporada', 'Equipo',
        function($scope, $stateParams, $modalInstance, entity, Temporada, Equipo) {

        $scope.temporada = entity;
        $scope.equipos = Equipo.query();
        $scope.load = function(id) {
            Temporada.get({id : id}, function(result) {
                $scope.temporada = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:temporadaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.temporada.id != null) {
                Temporada.update($scope.temporada, onSaveFinished);
            } else {
                Temporada.save($scope.temporada, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
