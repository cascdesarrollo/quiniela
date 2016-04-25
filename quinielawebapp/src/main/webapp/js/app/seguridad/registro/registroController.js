angular.module('quiniela.registro', ['ngCookies', 'registroServices'])
        .controller('RegistroCtrl', function ($scope, $window, $location, factoryRegistroService,
                translationService, $cookies) {

            if (!$cookies.get('csrftoken')) {
               $window.location.href = 'index.html';
            }

            $scope.consultando = false;
            $scope.auth_token = null;
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

            $scope.save = function () {
                
                $location.path("/login");
                return;
                $scope.mensajeError = null;
                $scope.muestraMensajeError = false;
                if($scope.password!==$scope.rePassword){
                    $scope.mensajeError = "Contraseña No Coincide";
                    $scope.muestraMensajeError = true;
                    return;
                }
                factoryRegistroService.registrar($scope.nombre, $scope.telefono, $scope.email, $scope.password)
                        .success(function (data) {
                            $scope.process.modal('hide');
                            alert('Usuario Registrado exitosamente!');
                            
                        }).error(function (data) {
                    $scope.process.modal('hide');
                    $scope.mensajeError = 'Error en registro de usuario';
                    $scope.muestraMensajeError = true;
                });
            };




            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();

        });