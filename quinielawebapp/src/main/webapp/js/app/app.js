angular.module('quiniela', ['ngRoute', 'ngResource', 'ngCookies',
    'chat',
    'ngAnimate',
    'generalServices',
    'quiniela.registro',
    'quiniela.login',
    'quiniela.agregar',
    'quiniela.quinielas'

])
        .constant('config', {
            //
            // Get your PubNub API Keys in link below phone demo.
            //
            "pubnub": {
                "publish-key": "pub-c-b03f67aa-8528-4bef-8789-5cb81800898a",
                "subscribe-key": "sub-c-a8bf11fe-0b03-11e6-a6c8-0619f8945a4f"
            }
        })
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
                USUARIO = '';
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
                                    USUARIO = data.nombre;
                                } else {
                                    process.modal('hide');
                                }
                            }).error(function (data) {
                        $scope.cerrarSesion();
                        process.modal('hide');

                    });
                } else {
                    USUARIO = '';
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
                ['Messages', '$scope', 'factoryGeneralService', 'translationService',
                    function (Messages, $scope,
                            factoryGeneralService, translationService) {
                        $scope.tablaList = [];
                        $scope.listadoPosiciones = function () {
                            factoryGeneralService.tabla('A')
                                    .success(function (data) {
                                        $scope.tablaList = data;
                                    }).error(function (data) {
                            });
                        };
                        $scope.listadoPosiciones();


                        $scope.logeado=false;
                        if (USUARIO && USUARIO !== '') {
                            $scope.logeado=true;
                            $scope.usuario = {id: uuid(), name: 'Carlitos'};
                            Messages.user($scope.usuario);
                        }

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
                            }
                        };

                        $scope.translate = function () {
                            translationService.getTranslation($scope, $scope.selectedLanguage);
                        };
                        $scope.selectedLanguage = IDIOMA;
                        $scope.translate();

                    }]);
var IDIOMA = 'es';
var QUINIELA = 'http://localhost:8080/quinielaservice/webresources/';
var USUARIO = '';
function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
            function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
}