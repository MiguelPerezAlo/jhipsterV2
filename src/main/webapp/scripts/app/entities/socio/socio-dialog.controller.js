'use strict';

angular.module('jhipsterApp').controller('SocioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Socio', 'Equipo',
        function($scope, $stateParams, $modalInstance, entity, Socio, Equipo) {

        $scope.socio = entity;
        $scope.equipos = Equipo.query();
        $scope.load = function(id) {
            Socio.get({id : id}, function(result) {
                $scope.socio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:socioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.socio.id != null) {
                Socio.update($scope.socio, onSaveFinished);
            } else {
                Socio.save($scope.socio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
