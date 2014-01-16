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
        if (!result.valid) {
          $('.messages.error').text(JSON.stringify(result)).show();
          return result;
        }
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
    'change select': 'onChangeType',
    'change input[name=maneColor]': 'onChangeManeColor',
    'change input[name=stripeCount]': 'onChangeStripeCount',
    'change input[name=hibernationDays]': 'onChangeHibernationDays',
    'click input[type=button]': 'onSubmit'
  },

  onChangeType: function(e) {
    this.model.set('_t', $(e.currentTarget).val());
    this.model.unset('maneColor');
    this.model.unset('stripeCount');
    this.model.unset('hibernationDays');
    this.render();
  },

  onChangeManeColor: function(e) {
    this.model.set('maneColor', $(e.currentTarget).val());
  },

  onChangeStripeCount: function(e) {
    this.model.set('stripeCount', parseInt($(e.currentTarget).val(), 10));
  },

  onChangeHibernationDays: function(e) {
    this.model.set('maneColor', $(e.currentTarget).val());
  },

  onSubmit: function(e) {
    $('.messages').hide();
    var xhr = this.model.save();
    if (xhr) {
      xhr.success(function() {
        $('.messages.success').text("Success!").show();
      });
    }
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
