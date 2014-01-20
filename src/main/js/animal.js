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
        isBear: this.model.get('_t') === 'bear',
        shouldValidateOnClient: this.shouldValidateOnClient
    };
  },

  events: {
    'change select': 'onChangeType',
    'change input[name=maneColor]': 'onChangeManeColor',
    'change input[name=stripeCount]': 'onChangeStripeCount',
    'change input[name=hibernationDays]': 'onChangeHibernationDays',
    'click input[name=shouldValidateOnClient]': 'onClickShouldValidateOnClient',
    'click input[type=button]': 'onSubmit'
  },

  modelEvents: {
    'invalid': 'onInvalid',
    'error': 'onError'
  },

  shouldValidateOnClient: true,

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

  onClickShouldValidateOnClient: function(e) {
    this.shouldValidateOnClient = $(e.currentTarget).is(':checked');
  },

  onSubmit: function(e) {
    $('.messages').hide();
    var options = this.shouldValidateOnClient ? {} : { validate: false },
        xhr = this.model.save(undefined, options);
    if (xhr) {
      xhr.success(function() {
        $('.messages.success').text("Success!").show();
      });
    }
  },

  onInvalid: function(model, error) {
    $('.messages.error').text('CLIENT: '+JSON.stringify(error)).show();
  },

  onError: function(model, xhr) {
    var error = JSON.parse(xhr.responseText);
    $('.messages.error').text('SERVER: '+JSON.stringify(error)).show();
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
