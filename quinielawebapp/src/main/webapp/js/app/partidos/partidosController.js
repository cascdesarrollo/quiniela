angular.module('quiniela.partidos', ['ngRoute', 'ngResource', 'ngCookies', 'partidosServices'])
        .controller('PartidosCtrl', function ($scope, $cookies, $location, $window, factoryPartidosService, translationService) {

            if (!$cookies.get('csrftoken')) {
                $window.location.href = 'index.html';
            }

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
                    + 'Cargando Datos de Partidos'
                    + '<div class="progress">'
                    + '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 100%">'
                    + '<span class="sr-only">10% Complete</span>'
                    + '</div>'
                    + '</div>'

                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '</div>');


            factoryPartidosService.partidos($cookies.get('csrftoken'))
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

            $scope.guardar = function (item) {
                console.log(item);

                var cadena = "el resultado a guardar es \n" +
                        item.idEquipo1.equipo + "  " + item.golEquipo1
                        + "\n"
                        + item.idEquipo2.equipo + "  " + item.golEquipo2;
                alert(cadena);
                if (confirm("Seguro desea guardar el resultado \n" + cadena)) {
                    factoryPartidosService.guardar($cookies.get('csrftoken'),
                            $scope.email, $scope.password,
                            item.id, item.golEquipo1, item.golEquipo2)
                            .success(function (data) {
                                alert("Resultado guardado exitosamente");
                                $window.location.href = 'index.html';
                            }).error(function (data) {
                        if (data.error) {
                            $scope.muestraMensajeError = data.error;
                            $scope.mensajeError = data.des_error;
                        } else {
                            $scope.muestraMensajeError = true;
                            $scope.mensajeError = "Error Consultando BackEnd";
                        }
                    });
                }
            }

            $scope.regresar = function () {
                $location.path("/");
            };


            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();

        });