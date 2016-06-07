angular.module('quiniela', ['ngRoute', 'ngResource', 'ngCookies',
    //'chat',
    'ngAnimate',
    'generalServices',
    'quiniela.registro',
    'quiniela.login',
    'quiniela.agregar',
    'quiniela.quinielas',
    'quiniela.partidos'
])
        /*.constant('config', {
         //
         // Get your PubNub API Keys in link below phone demo.
         //
         "pubnub": {
         "publish-key": "pub-c-b03f67aa-8528-4bef-8789-5cb81800898a",
         "subscribe-key": "sub-c-a8bf11fe-0b03-11e6-a6c8-0619f8945a4f"
         }
         })*/
        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/', {
                    templateUrl: 'dashboard.html',
                    controller: 'DashBoardCtrl'
                });
                $routeProvider.when('/:id/:alias', {
                    templateUrl: 'pages/quinielas/detalle.html',
                    controller: 'QuinielasCtrl'
                });
                $routeProvider.when('/registro', {
                    templateUrl: 'pages/seguridad/registro.html',
                    controller: 'RegistroCtrl'
                });
                $routeProvider.when('/login', {
                    templateUrl: 'pages/seguridad/login.html',
                    controller: 'LoginCtrl'
                });
                $routeProvider.when('/agregar', {
                    templateUrl: 'pages/quinielas/registrar.html',
                    controller: 'AgregarCtrl'
                });
                $routeProvider.when('/misquinielas', {
                    templateUrl: 'pages/quinielas/usuario/misquinielas.html',
                    controller: 'MisQuinielasCtrl'
                });
                $routeProvider.when('/misquinielas/:id/:alias', {
                    templateUrl: 'pages/quinielas/usuario/editar.html',
                    controller: 'QuinielasCtrl'
                });
                $routeProvider.when('/partidos', {
                    templateUrl: 'pages/partidos/resultado.html',
                    controller: 'PartidosCtrl'
                });
                $routeProvider.when('/reglas', {
                    templateUrl: 'pages/reglas.html'
                });
                $routeProvider.otherwise({redirectTo: '/'});
            }])
        .controller('MainCtrl', function ($scope, $cookies, $window, $location,
                factoryGeneralService, translationService
                ) {
            $scope.cerrarSesion = function () {
                factoryGeneralService.salir(
                        $cookies.get('csrftoken')
                        )
                        .success(function (data) {
                        }).error(function (data) {
                });
                $cookies.remove('csrftoken');
                $scope.dataSes = {};
                $location.path("#/");
            };

            var process = $(
                    '<div class="modal fade" id="process" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static"  data-keyboard="false" >'


                    + '<div class="modal-dialog" >'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<h4 class="modal-title" id="myModalLabel">Procesando</h4>'
                    + '</div>'
                    + '<div class="modal-body">'
                    + 'Cargando Datos de Usuario'
                    + '<div class="progress">'
                    + '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 100%">'
                    + '<span class="sr-only">10% Complete</span>'
                    + '</div>'
                    + '</div>'

                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '</div>');

            $scope.dataSes = {};
            $scope.validaSesion = function () {
                if ($cookies.get('csrftoken')) {
                    factoryGeneralService.datos($cookies.get('csrftoken'))
                            .success(function (data) {
                                if (data) {
                                    process.modal('hide');
                                    $scope.dataSes = data;
                                } else {
                                    process.modal('hide');
                                }
                            }).error(function (data) {
                        $scope.cerrarSesion();
                        process.modal('hide');

                    });
                } else {
                    process.modal('hide');
                }
            };
            $scope.validaSesion();

            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();
        })
        .controller('DashBoardCtrl',
                [
                    //'Messages',  era para chat
                    '$scope', '$cookies', 'factoryGeneralService', 'translationService',
                    function (
                            //Messages, era para chat
                            $scope, $cookies,
                            factoryGeneralService, translationService) {
                        $scope.pendientes = 0;
                        $scope.verificadas = 0;
                        $scope.primero = 0;
                        $scope.segundo = 0;
                        $scope.tercero = 0;
                        $scope.tablaList = [];
                        $scope.contabilizar = function () {
                            $scope.pendientes = 0;
                            $scope.verificadas = 0;
                            angular.forEach($scope.tablaList, function (quiniela) {
                                if (quiniela.status === 'P') {
                                    $scope.pendientes++;
                                } else if (quiniela.status === 'V') {
                                    $scope.verificadas++;
                                }
                            });
                            var recaudado = $scope.verificadas * 1000;
                            $scope.primero = (recaudado * (0.4));
                            $scope.segundo = (recaudado * (0.2));
                            $scope.tercero = (recaudado * (0.1));
                        };
                        $scope.listadoPosiciones = function () {
                            factoryGeneralService.tabla('A')
                                    .success(function (data) {
                                        $scope.tablaList = data;
                                        $scope.contabilizar();
                                    }).error(function (data) {
                            });
                        };
                        $scope.listadoPosiciones();
                        $scope.logeado = false;
                        $scope.dataSes = {};
                        $scope.validaSesion = function () {
                            if ($cookies.get('csrftoken')) {
                                factoryGeneralService.datos($cookies.get('csrftoken'))
                                        .success(function (data) {
                                            if (data) {
                                                $scope.logeado = true;
                                                $scope.usuario = {id: uuid(), name: data.nombre.replace(" ", "_")};
                                                Messages.user($scope.usuario);
                                            }
                                        }).error(function (data) {
                                });
                            }
                        };
                        $scope.validaSesion();

                        $scope.mensajePrivacidad = function () {
                            bootbox.alert("Por privacidad, los resultados de quinielas de otras personas solo podran verse a partir del dia de inicio de la copa", function () {
                            });
                        };

                        /* CHAT
                         // Message Inbox
                         $scope.messages = [];
                         // Receive Messages
                         Messages.receive(function (message) {
                         $scope.messages.push(message);
                         });
                         // Send Messages
                         $scope.send = function () {
                         if ($scope.textbox !== '') {
                         Messages.send({data: $scope.textbox});
                         $scope.textbox = '';
                         $scope.abajo();
                         }
                         };
                         
                         $scope.checkIfEnterKeyWasPressed = function ($event) {
                         var keyCode = $event.which || $event.keyCode;
                         if (keyCode === 13) {
                         $scope.send();
                         }
                         };
                         
                         $scope.abajo = function () {
                         var elem = document.getElementById('scrollMensajes');
                         elem.scrollTop = elem.scrollHeight + 150;
                         };
                         $scope.abajo();
                         FIN CHAT */

                        $scope.translate = function () {
                            translationService.getTranslation($scope, $scope.selectedLanguage);
                        };
                        $scope.selectedLanguage = IDIOMA;
                        $scope.translate();
                    }]);
var IDIOMA = 'es';
var QUINIELA = 'http://localhost:8080/quinielaservice/webresources/';
//var QUINIELA = 'http://54.214.255.80:9090/quinielaservice/webresources/';
function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
            function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
}
