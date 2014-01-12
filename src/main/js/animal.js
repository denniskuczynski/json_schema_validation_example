var Animal = Backbone.Model.extend({
    defaults: {
        '_t': 'lion',
        'maneColor': 'brown',
        'stripeCount': undefined,
        'hibernationDays': undefined
    },

    url: 'service/animals/suggest',

    initialize: function(attributes, options) {
        this.schema = options.schema;
    },

    validate: function(attributes, options) {
        var result = tv4.validateMultiple(attributes, this.schema);
        console.log(result);
    }
});

var AnimalForm = Marionette.ItemView.extend({
  template: JST['src/main/hbs/animal.hbs'],

  templateHelpers: function() {
    return {
        isLion: this.model.get('_t') === 'lion',
        isTiger: this.model.get('_t') === 'tiger',
        isBear: this.model.get('_t') === 'bear'
    };
  },

  events: {
    'change select': 'onChangeSelect',
    'click input[type=button]': 'onSubmit'
  },

  onChangeSelect: function(e) {
    this.model.set('_t', $(e.currentTarget).val());
    this.model.unset('maneColor');
    this.model.unset('stripeCount');
    this.model.unset('hibernationDays');
    this.render();
  },

  onSubmit: function(e) {
    this.model.save();
    this.render();
  }
});

$(document).ready(function() {
  var xhr = $.getJSON('/schemas/animal.json');
  $.when(xhr).then(function(data) {
    var animalForm = new AnimalForm({
      model: new Animal({}, {
        schema: data
      })
    });
    $("#content").append(animalForm.render().el);
  });
});
