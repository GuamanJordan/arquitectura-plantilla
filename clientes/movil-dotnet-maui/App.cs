namespace Arquitectura.ClienteMovilMaui;

public class App : Application
{
    private readonly MainPage mainPage;

    public App(MainPage mainPage)
    {
        this.mainPage = mainPage;
    }

    protected override Window CreateWindow(IActivationState? activationState)
    {
        return new Window(new NavigationPage(mainPage));
    }
}
