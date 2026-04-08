# Contributing to ComposeAnimatedIcons

Thank you for helping make **ComposeAnimatedIcons** better! We welcome all contributions, from bug fixes to new animated icons.

## Report a Bug

Found a bug? Let us know!

1. Check existing issues to avoid duplicates.
2. Open a new issue with:
  - A clear title and description.
  - Steps to reproduce the bug.
  - A minimal example or screenshot (if possible).

**Want to contribute a fix?** If you’re up for it, fork the repo, fix the bug, and submit a PR, we’d love your help!

## Add a New Icon

1. **Fork the repository** and create a new branch (e.g., `feature/new-icon`).
2. **Set up the project locally:**
  - Open the project and run the app to test your changes.
3. **Add your icon** in the `composeApp/src/commonMain/kotlin/com/valebell/composeanimatedicons/icons/` directory.
  - Name your file `[IconName]Icon.kt` (e.g., `ClockIcon.kt`).
4. **Test your icon** locally by launching the task `:composeApp:wasmJsBrowserDevelopmentRun`.
5. **Submit a PR** to the `dev` branch, linking to the issue it resolves (or just describe your changes if no issue exists).
  - Use a clear title (e.g., `feat: add animated clock icon`).
  - Include a GIF of your new icon.

---

## Pull Request Guidelines

- Link your PR to an issue or describe your changes clearly.
- Include a GIF / screenshot of your new icon or fixed bug.
- Follow the project’s coding style (check existing icons for reference).
- Add tests if applicable.
- Be patient, we’ll review your PR as soon as possible!

---

Thanks so much for your support!
