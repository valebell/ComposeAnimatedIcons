# ComposeAnimatedIcons

ComposeAnimatedIcons is hosting a collection of reusable, animated icon components written in Kotlin for Jetpack Compose.
Each icon is provided as a standalone .kt file, ready to be copied and pasted directly into your Jetpack Compose project, no external dependencies or setup required.

## What’s Inside?

- **Ready-to-use icons:** A library of animated icons.
- **Copy-paste components:** Each icon is a self-contained Kotlin function, making integration effortless.
- **Customizable:** Adjust animation speed, color, size with simple parameters.
- **Cross-platform:** Icons work in Android, iOS, Web, and Desktop projects using Jetpack Compose.

## Live Demo

Try it now: [https://valebell.github.io/ComposeAnimatedIcons/](https://valebell.github.io/ComposeAnimatedIcons/)

<img width="1231" height="787" alt="AnimatedIcons" src="https://github.com/user-attachments/assets/9bed6c0c-54ee-4dc4-8b2e-8c12fe6669b3" />


### Running the project locally

```bash
# Clone the repository
git clone https://github.com/valebell/ComposeAnimatedIcons.git

# Run the web demo
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Then open `http://localhost:8080` in your browser.

## Customization

Each icon supports customization:

```kotlin
Rocket(
    animate = true,                // Control animation state
    shouldLoop = true,             // Loop animation continuously
    tint = Color.Blue,             // Custom color
    contentDescription = "Launch", // Accessibility
    loopDelayMs = 1000             // Delay between loops
)
```

## Contributing

Want to add your own animated icon? We'd love your contribution!
See [CONTRIBUTING.md](CONTRIBUTING.md) for full guidelines.

## License

MIT License - see [LICENSE](LICENSE) for details.

---

Made with ❤️ using Kotlin Multiplatform and Jetpack Compose
