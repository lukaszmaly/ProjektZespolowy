from django.conf.urls import patterns, include, url

urlpatterns = patterns('',
    url(r'^$', 'magic.views.gracze'),
    url(r'^(?P<gracz_id>\d+)/$', 'magic.views.gracz'),
    url(r'^language/(?P<language>[a-z\-]+)/$', 'magic.views.language'),
    #urlr(r'^stat/$', 'magic.views.stat'),
    
)
