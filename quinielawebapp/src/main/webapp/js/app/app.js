angular.module('quiniela', ['ngRoute', 'ngResource', 'ngCookies',
    'ngAnimate',
    'generalServices',
    'quiniela.registro',
    'quiniela.login',
    'quiniela.agregar'

])
        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/', {
                    templateUrl: 'dashboard.html',
                    controller: 'DashBoardCtrl'
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

            $scope.pageClass = 'page-back';


            $scope.cerrarSesion = function () {
                factoryGeneralService.salir(
                        $cookies.get('csrftoken')
                        )
                        .success(function (data) {
                        }).error(function (data) {
                });
                $cookies.remove('csrftoken');
                $scope.dataSes={};
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
                    console.log('hay corazon');
                    factoryGeneralService.datos($cookies.get('csrftoken'))
                            .success(function (data) {
                                console.log('siii');
                                if (data) {
                                    process.modal('hide');
                                    $scope.dataSes = data;
                                    console.log($scope.dataSes);
                                } else {
                                    process.modal('hide');
                                }
                            }).error(function (data) {
                                console.log('no!!!');
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
        .controller('DashBoardCtrl', function ($scope, $cookies, $window,
                factoryGeneralService, translationService) {
            $scope.pageClass = 'page-back';



            $scope.translate = function () {
                translationService.getTranslation($scope, $scope.selectedLanguage);
            };
            $scope.selectedLanguage = IDIOMA;
            $scope.translate();

        });
var IDIOMA = 'es';
var QUINIELA = 'http://localhost:8080/quinielaservice/webresources/';
        