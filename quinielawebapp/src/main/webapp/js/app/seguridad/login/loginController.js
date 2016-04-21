angular.module('quiniela.login', ['ngRoute', 'ngResource', 'ngCookies', 'loginServices'])
        .controller('LoginCtrl', function ($scope, $window, $location, factoryLoginService, translationService, $cookies) {
            if ($cookies.get('csrftoken')) {
                $window.location.href = 'index.html';
            }
            $scope.empresasList = {};
            $scope.empresa = null;
            $scope.consultando = false;
            $scope.auth_token = null;
            $scope.mensajeError = null;
            $scope.muestraMensajeError = false;
            $scope.validateUser = function () {
                $scope.process.modal('show');
                $scope.consultando = true;
                $scope.mensajeError = null;
                $scope.muestraMensajeError = false;
                factoryLoginService.validar($scope.email, $scope.password)
                        .success(function (data) {
                            $scope.process.modal('hide');
                            console.log(data);
                            $scope.auth_token = data.auth_token;
                            $cookies.put('csrftoken', data.auth_token);
                            $scope.consultando = false;
                            
                            $window.location.href = 'index.html';
                            //$location.path("#/");
                            
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
            };


            $scope.process = $(
                    '<div class="modal fade" id="process" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static"  data-keyboard="false" >'


                    + '<div class="modal-dialog" >'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<h4 class="modal-title" id="myModalLabel">Procesando</h4>'
                    + '</div>'
                    + '<div class="modal-body">'
                    + 'Verificando Credenciales'
                    + '<div class="progress">'
                    + '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 100%">'
                    + '<span class="sr-only">10% Complete</span>'
                    + '</div>'
                    + '</div>'

                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '</div>');


            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();

        });