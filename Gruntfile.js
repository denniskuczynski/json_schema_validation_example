module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    clean: ["dist"],
    jshint: {
      all: ['Gruntfile.js', 'src/main/js/**/*.js']
    },
    handlebars: {
      compile: {
        options: {
          namespace: "JST"
        },
        files: {
          "dist/handlebars-templates.js": ["src/main/hbs/**/*.hbs"]
        }
      }
    },
    concat: {
      js: {
        src: ['components/jquery/jquery.min.js',
              'components/underscore/underscore-min.js',
              'components/backbone/backbone-min.js',
              'components/backbone.marionette/lib/backbone.marionette.min.js',
              'components/handlebars/handlebars.runtime.min.js',
              'components/tv4/tv4.js',
              'dist/handlebars-templates.js',
              'src/main/js/**/*.js'],
        dest: 'src/main/resources/assets/<%= pkg.name %>.js'
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-handlebars');
  grunt.loadNpmTasks('grunt-contrib-concat');

  // Default task(s).
  grunt.registerTask('default', ['jshint', 'handlebars', 'concat']);

};