angular.module('quiniela.quinielas', ['ngRoute', 'ngResource', 'ngCookies', 'quinielasServices'])
        .controller('MisQuinielasCtrl',
                ['$scope', '$cookies', '$window', 'factoryQuinielasService', 'translationService',
                    function ($scope, $cookies, $window,
                            factoryQuinielasService, translationService) {

                        if (!$cookies.get('csrftoken')) {
                            $window.location.href = 'index.html';
                        }

                        $scope.tablaList = [];
                        $scope.listadoQuinielas = function () {
                            factoryQuinielasService.listaQuinielas($cookies.get('csrftoken'))
                                    .success(function (data) {
                                        $scope.tablaList = data;
                                    }).error(function (data) {
                            });
                        };
                        $scope.listadoQuinielas();

                        $scope.translate = function () {
                            translationService.getTranslation($scope, $scope.selectedLanguage);
                        };
                        $scope.selectedLanguage = IDIOMA;
                        $scope.translate();
                    }])
        .controller('QuinielasCtrl', function ($scope, $routeParams, $location, $uibModal, factoryQuinielasService, translationService) {
            if (!$routeParams.id) {
                $location.path("/");
            }
            $scope.id = $routeParams.id;
            $scope.alias = $routeParams.alias;

            $scope.mensajeError = null;
            $scope.muestraMensajeError = false;

            $scope.process = $(
                    '<div class="modal fade" id="process" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static"  data-keyboard="false" >'


                    + '<div class="modal-dialog" >'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<h4 class="modal-title" id="myModalLabel">Procesando</h4>'
                    + '</div>'
                    + '<div class="modal-body">'
                    + 'Registrando Datos'
                    + '<div class="progress">'
                    + '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 100%">'
                    + '<span class="sr-only">10% Complete</span>'
                    + '</div>'
                    + '</div>'

                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '</div>');


            factoryQuinielasService.detalle($scope.id)
                    .success(function (data) {
                        $scope.process.modal('hide');
                        $scope.tablaList = data;
                    }).error(function (data) {
                $scope.process.modal('hide');
                $scope.consultando = false;
                if (data) {
                    $scope.muestraMensajeError = data.error;
                    $scope.mensajeError = data.des_error;
                } else {
                    $scope.muestraMensajeError = true;
                    $scope.mensajeError = "Error Consultando BackEnd";
                }
            });

            $scope.regresar = function () {
                $location.path("/");
            };

            $scope.regresarMis = function () {
                $location.path("/misquinielas");
            };

            $scope.open = function () {
                var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        tablaList: function () {
                            return $scope.tablaList;
                        }
                    }
                });
            };

            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();
        });
angular.module('quiniela.quinielas').controller('ModalInstanceCtrl', function ($scope, $cookies, $location, $uibModalInstance, factoryQuinielasService,
        tablaList) {
    $scope.process = $(
            '<div class="modal fade" id="process" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static"  data-keyboard="false" >'


            + '<div class="modal-dialog" >'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<h4 class="modal-title" id="myModalLabel">Procesando</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + 'Registrando Datos'
            + '<div class="progress">'
            + '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 100%">'
            + '<span class="sr-only">10% Complete</span>'
            + '</div>'
            + '</div>'

            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>');

    $scope.ok = function () {
        $scope.process.modal('show');
        factoryQuinielasService.guardar($cookies.get('csrftoken'), tablaList)
                .success(function (data) {
                    $scope.process.modal('hide');
                    alert("Quiniela Actualizada Exitosamente ");
                    $location.path("/misquinielas");
                }).error(function (data) {
            $scope.process.modal('hide');
        });

        $uibModalInstance.dismiss('cancel');
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
