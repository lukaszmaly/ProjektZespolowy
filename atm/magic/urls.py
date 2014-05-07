from django.conf.urls import patterns, include, url

urlpatterns = patterns('',
    url(r'^contact/$', 'magic.views.contact'),
    url(r'^$', 'magic.views.gracze'),
    url(r'^(?P<gracz_id>\d+)/$', 'magic.views.gracz'),
    url('(?P<gracz_id>\d+)/(?P<part>\d+)/$','magic.views.gra'),
    url(r'^language/(?P<language>[a-z\-]+)/$', 'magic.views.language'),
    
)
